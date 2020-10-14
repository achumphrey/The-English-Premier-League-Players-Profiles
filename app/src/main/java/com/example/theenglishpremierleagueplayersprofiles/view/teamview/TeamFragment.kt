package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.NetworkConnection
import com.example.theenglishpremierleagueplayersprofiles.dagger.DaggerNetworkComponent
import com.example.theenglishpremierleagueplayersprofiles.dagger.NetworkModule
import com.example.theenglishpremierleagueplayersprofiles.dagger.RepositoryModule
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerFragment
import kotlinx.android.synthetic.main.team_fragment.*
import javax.inject.Inject

class TeamFragment : Fragment() {

    @Inject
    lateinit var teamModelViewFactory: TeamModelViewFactory
    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: TeamAdapter

    private var listener:OnteamClickListener =  object : OnteamClickListener {
        override fun onTeamClick(teams: Teams) {
            val bundle = Bundle()
            bundle.putString("message", teams.idTeam.toString())
            val fragobj = PlayerFragment()
            fragobj.arguments = bundle

            val transaction = activity
                ?.supportFragmentManager!!
                .beginTransaction()
            transaction.replace(R.id.frm_container, fragobj)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (isAdded){
            Log.i("IsAdded?", "$isAdded")
            NetworkConnection.getComponent().inject(this)
        }
        val checkInternet: Boolean = NetworkConnection.checkConnection()
        Log.i(TAGTFG_CHK_INT, checkInternet.toString())

    //    getNetworkComponent()

        viewModel = ViewModelProviders.of(this, teamModelViewFactory)
            .get(TeamViewModel::class.java)

        val teams : MutableLiveData<List<Teams>>? = viewModel.onShowTeamList()
        val displayProgress : MutableLiveData<Boolean>? = viewModel.getShowProgress()
        val teamDB : MutableLiveData<List<Teams>>? = viewModel.onShowDBTeam()
    //    val showDBGetProgress : MutableLiveData<Boolean>? = viewModel.getShowDBGetSuccess()
        val showDBAddSuccess: MutableLiveData<Boolean>? = viewModel.getShowDBAddSuccess()

        // Check for network connection
        if (checkInternet) viewModel.getTeamRecords() else viewModel.getTeamsFromDB()

        // Call to DB
        teamDB?.observe(this, object : Observer<List<Teams>>{
            override fun onChanged(t: List<Teams>) {
                Log.i(TAGTFGDB, t[0].strTeam)

                val adapter =
                    TeamDBAdapter(
                        t,
                        object :
                            OnTeamDBClickLister {
                            override fun onTeamClick(teams: Teams) {

                                val bundle = Bundle()
                                bundle.putString("message", teams.idTeam.toString())
                                val fragobj = PlayerFragment()
                                fragobj.arguments = bundle

                                val transaction = activity
                                    ?.supportFragmentManager!!
                                    .beginTransaction()
                                transaction.replace(R.id.frm_container, fragobj)
                                    .addToBackStack(null)
                                    .commit()
                            }
                        })
                rv_list.layoutManager = LinearLayoutManager(activity)
                rv_list.adapter = adapter

            }
        })//end of DB call

        // Call to network
        displayProgress?.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t == false)
                    prgs_bar.visibility = View.GONE
                else
                    prgs_bar.visibility = View.VISIBLE
            }
        })

        teams?.observe(this, object: Observer<List<Teams>>{
            override fun onChanged(t: List<Teams>?) {
                Log.i(TAGTFGNW,t!![0].strTeam)
                adapter.updateTeamList(t)
            }
        })//end of network call

        showDBAddSuccess?.observe(this, object : Observer<Boolean>{
            override fun onChanged(t: Boolean?) {
                if (t == true)
                    Toast.makeText(activity,"Successfully add",Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(activity,"Not Successfull",Toast.LENGTH_LONG).show()
            }
        })

        return inflater.inflate(R.layout.team_fragment, container, false)
    }

    fun setUpTeamRecyclerView(){
        adapter = TeamAdapter(mutableListOf(), listener)
        rv_list.layoutManager = LinearLayoutManager(activity)
        rv_list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpTeamRecyclerView()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        NetworkConnection.getComponent().inject(this)
    }

    private fun getNetworkComponent(){
        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(activity!!.application))
            .repositoryModule(RepositoryModule())
            .build()
            .inject(this)
    }

    // Check for Network Connection
    private fun amIConnected(): Boolean {
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network: Network? = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager
                .getNetworkCapabilities(network) //activeNetworkInfo
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }else{
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    companion object{
        const val TAGTFGNW = "TeamFragmentNW"
        const val TAGTFGDB = "TeamFragmentDB"
        const val TAGTFG_CHK_INT = "oncreateViewTeamFrg"
    }
}

