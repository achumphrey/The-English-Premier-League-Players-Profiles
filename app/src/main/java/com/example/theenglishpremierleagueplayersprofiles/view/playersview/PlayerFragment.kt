package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.content.Context
import android.net.ConnectivityManager
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
import com.example.theenglishpremierleagueplayersprofiles.dagger.DaggerNetworkComponent
import com.example.theenglishpremierleagueplayersprofiles.dagger.NetworkModule
import com.example.theenglishpremierleagueplayersprofiles.dagger.PlayerRepoModule
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview.PlayersDetailsFragment
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.OnTeamClickLister
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.OnTeamDBClickLister
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamAdapter
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamDBAdapter
import kotlinx.android.synthetic.main.player_fragment.*
import kotlinx.android.synthetic.main.player_fragment.prgs_bar
import kotlinx.android.synthetic.main.team_fragment.*
import javax.inject.Inject

class PlayerFragment : Fragment() {

    @Inject
    lateinit var playerModelViewFactory: PlayerModelViewFactory
    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var checkInternet: Boolean = amIConnected()
        Log.i("oncreatePlayerFrg", "$checkInternet")

        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(activity!!.application))
            .playerRepoModule(PlayerRepoModule())
            .build().inject(this)

        viewModel = ViewModelProviders.of(this, playerModelViewFactory).get(PlayerViewModel::class.java)

        val playerList : MutableLiveData<PlayersListModel>? = viewModel.onShowPlayerList()
        val displayProgress : MutableLiveData<Boolean>? = viewModel.getShowProgress()
        val playersDB : MutableLiveData<List<Player>>? = viewModel.onShowDBPlayers()
        val showDBGetProgress : MutableLiveData<Boolean>? = viewModel.getShowDBGetSuccess()
        val showDBAddSuccess: MutableLiveData<Boolean>? = viewModel.getShowDBAddSuccess()
        var teamId = arguments?.getString("message")
        var teamIdInt: Int = Integer.parseInt(teamId!!)


        // Check for network connection
        if (checkInternet)  viewModel.getAllTeamPlayers(teamId) else viewModel.getTeamPlayersFromDB(teamIdInt)

       // Call to DB
        playersDB?.observe(this, object : Observer<List<Player>>{
            override fun onChanged(t: List<Player>) {
                Log.i("PlayerFragmentDBGet", "${t?.get(0).strPlayer}")

                val adapter =
                    PlayerDBAdapter(
                        t!!,
                        object :
                            OnPlayerDBClickListener {
                            override fun onPlayerDBClick(players: Player) {

                                var bundle = Bundle();
                                bundle.putString("message", players.idPlayer.toString());
                                var fragobj: PlayersDetailsFragment = PlayersDetailsFragment()
                                fragobj!!.arguments = bundle

                                var transaction: FragmentTransaction = getActivity()
                                    ?.getSupportFragmentManager()!!
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

        playerList?.observe(this, object: Observer<PlayersListModel> {
            override fun onChanged(t: PlayersListModel?) {
                Log.i("PlayerFragmentNW","${t!!.player[1].strPlayer}")

                val adapter: PlayersAdapter =
                    PlayersAdapter(
                        t!!,
                        object :
                            OnPlayerClickListener {
                            override fun onPlayerClick(players: Player) {

                                var bundle = Bundle();
                                bundle.putString("message", players.idPlayer.toString());
                                var fragobj: PlayersDetailsFragment = PlayersDetailsFragment()
                                fragobj!!.arguments = bundle

                                var transaction: FragmentTransaction = getActivity()
                                    ?.getSupportFragmentManager()!!
                                    .beginTransaction()
                                transaction.replace(R.id.frm_container, fragobj)
                                    .addToBackStack(null)
                                    .commit()

                            }
                        })
                rv_player_list.layoutManager = LinearLayoutManager(activity)
                rv_player_list.adapter = adapter
            }
        })
        return inflater.inflate(R.layout.player_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    private fun amIConnected(): Boolean {
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
