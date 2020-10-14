package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.NetworkConnection
import com.example.theenglishpremierleagueplayersprofiles.dagger.DaggerNetworkComponent
import com.example.theenglishpremierleagueplayersprofiles.dagger.NetworkModule
import com.example.theenglishpremierleagueplayersprofiles.dagger.PlayerRepoModule
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview.PlayersDetailsFragment
import kotlinx.android.synthetic.main.player_fragment.*
import kotlinx.android.synthetic.main.player_fragment.prgs_bar
import javax.inject.Inject

class PlayerFragment : Fragment() {

    @Inject
    lateinit var playerModelViewFactory: PlayerModelViewFactory
    private lateinit var viewModel: PlayerViewModel
    private lateinit var playerAdapter: PlayersAdapter

    private var listener: OnPlayerClickListener = object : OnPlayerClickListener {
            override fun onPlayerClick(players: Player) {
                val bundle = Bundle()
                bundle.putString("message", players.idPlayer.toString())
                val fragobj = PlayersDetailsFragment()
                fragobj.arguments = bundle

                val transaction: FragmentTransaction = activity
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

        val checkInternet: Boolean = NetworkConnection.checkConnection()
     //   val checkInternet: Boolean = amIConnected()
        Log.i(TAGPFG_CHK_INT, "$checkInternet")

        getNetworkComponent()

        viewModel = ViewModelProviders.of(this, playerModelViewFactory)
            .get(PlayerViewModel::class.java)

        val playerList : MutableLiveData<List<Player>>? = viewModel.onShowPlayerList()
        val displayProgress : MutableLiveData<Boolean>? = viewModel.getShowProgress()
        val playersDB : MutableLiveData<List<Player>>? = viewModel.onShowDBPlayers()
    //    val showDBGetProgress : MutableLiveData<Boolean>? = viewModel.getShowDBGetSuccess()
    //    val showDBAddSuccess: MutableLiveData<Boolean>? = viewModel.getShowDBAddSuccess()
        val teamId = arguments?.getString("message")
        val teamIdInt: Int = Integer.parseInt(teamId!!)

        Log.i("Bundle Data", teamId)

        // Check for network connection
        if (checkInternet)  viewModel.getAllTeamPlayers(teamId) else viewModel
            .getTeamPlayersFromDB(teamIdInt)

       // Call to DB
        playersDB?.observe(this, object : Observer<List<Player>>{
            override fun onChanged(t: List<Player>) {
                Log.i(TAGPFGDB, t[0].strPlayer)

                val adapter =
                    PlayerDBAdapter(
                        t,
                        object :
                            OnPlayerDBClickListener {
                            override fun onPlayerDBClick(players: Player) {

                                val bundle = Bundle()
                                bundle.putString("message", players.idPlayer.toString())
                                val fragobj = PlayersDetailsFragment()
                                fragobj.arguments = bundle

                                val transaction: FragmentTransaction = activity
                                    ?.supportFragmentManager!!
                                    .beginTransaction()
                                transaction.replace(R.id.frm_container, fragobj)
                                    .addToBackStack(null)
                                    .commit()
                            }
                        })
                rv_player_list.layoutManager = LinearLayoutManager(activity)
                rv_player_list.adapter = adapter
            }
        })//end of DB call

       //Call to Network
        displayProgress?.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t == false)
                    prgs_bar.visibility = View.GONE
                else
                    prgs_bar.visibility = View.VISIBLE
            }
        })

        playerList?.observe(this, object: Observer<List<Player>> {
            override fun onChanged(t: List<Player>?) {
                Log.i(TAGPFGNW,t!![1].strPlayer)
                playerAdapter.updatePlayerList(t)
            }
        })// End of Network Call

        return inflater.inflate(R.layout.player_fragment, container, false)
    }

    fun setUpPlayerRecyclerView(){
        playerAdapter = PlayersAdapter(mutableListOf(), listener)
        rv_player_list.layoutManager = LinearLayoutManager(activity)
        rv_player_list.adapter = playerAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpPlayerRecyclerView()
    }

    private fun getNetworkComponent(){
        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(activity!!.application))
            .playerRepoModule(PlayerRepoModule())
            .build().inject(this)
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

    /*
    ConnectivityManager connectivityManager = (ConnectivityManager) context
    .getSystemService(Context.CONNECTIVITY_SERVICE)
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    Network network = connectivityManager.getActiveNetwork()
    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}
     */

    companion object{
        const val TAGPFGNW = "PlayerFragmentNW"
        const val TAGPFGDB = "PlayerFragmentDBGet"
        const val TAGPFG_CHK_INT = "oncreateViewPlayerFrg"
    }
}
