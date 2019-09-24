package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.dagger.DaggerNetworkComponent
import com.example.theenglishpremierleagueplayersprofiles.dagger.NetworkModule
import com.example.theenglishpremierleagueplayersprofiles.dagger.RepositoryModule
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerFragment
import kotlinx.android.synthetic.main.team_fragment.*
import javax.inject.Inject

class TeamFragment : Fragment() {

    @Inject
    lateinit var teamModelViewFactory: TeamModelViewFactory
    private lateinit var viewModel: TeamViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var checkInternet: Boolean = amIConnected()
        Log.i("oncreateTeamFrg", "$checkInternet")

        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(activity!!.application))
            .repositoryModule(RepositoryModule())
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, teamModelViewFactory).get(TeamViewModel::class.java)

        val teams : MutableLiveData<TeamsModel>? = viewModel.onShowTeamList()
        val displayProgress : MutableLiveData<Boolean>? = viewModel.getShowProgress()
        val teamDB : MutableLiveData<List<Teams>>? = viewModel.onShowDBTeam()
        val showDBGetProgress : MutableLiveData<Boolean>? = viewModel.getShowDBGetSuccess()
        val showDBAddSuccess: MutableLiveData<Boolean>? = viewModel.getShowDBAddSuccess()

        // Check for network connection
        if (checkInternet) viewModel.getTeamRecords() else viewModel.getTeamsFromDB()

        // Call to DB
        teamDB?.observe(this, object : Observer<List<Teams>>{
            override fun onChanged(t: List<Teams>) {
                Log.i("TeamFragmentDB", "${t?.get(0).strTeam}")

                val adapter: TeamDBAdapter =
                    TeamDBAdapter(
                        t!!,
                        object :
                            OnTeamDBClickLister {
                            override fun onTeamClick(teams: Teams) {

                                var bundle = Bundle();
                                bundle.putString("message", teams.idTeam.toString());
                                var fragobj: PlayerFragment = PlayerFragment()
                                fragobj!!.arguments = bundle

                                var transaction: FragmentTransaction = getActivity()
                                    ?.getSupportFragmentManager()!!
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

        teams?.observe(this, object: Observer<TeamsModel> {
            override fun onChanged(t: TeamsModel?) {
                Log.i("TeamFragmentNW","${t!!.teams[0].strTeam}")

                val adapter: TeamAdapter =
                                TeamAdapter(
                                    t!!,
                                    object :
                                        OnTeamClickLister {
                                        override fun onTeamClick(teams: Teams) {

                                var bundle = Bundle();
                                bundle.putString("message", teams.idTeam.toString());
                                var fragobj: PlayerFragment = PlayerFragment()
                                fragobj!!.arguments = bundle

                                var transaction: FragmentTransaction = getActivity()
                                    ?.getSupportFragmentManager()!!
                                    .beginTransaction()
                                transaction.replace(R.id.frm_container, fragobj)
                                    .addToBackStack(null)
                                    .commit()
                            }
                        })
                    rv_list.layoutManager = LinearLayoutManager(activity)
                    rv_list.adapter = adapter
            }
        })

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

   /* fun isConnectedToInternet(): Boolean {
        val connectivity = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }*/

    private fun amIConnected(): Boolean {
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

