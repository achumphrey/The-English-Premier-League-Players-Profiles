package com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.dagger.*
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import kotlinx.android.synthetic.main.fragment_players.*
import javax.inject.Inject

class PlayersDetailsFragment : Fragment() {

    @Inject
    lateinit var playerDetailsViewModelFactory: PlayerDetailsViewModelFactory
    private lateinit var viewModel: PlayerDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(activity!!.application))
            .playerRepoModule(PlayerRepoModule())
            .repositoryModule(RepositoryModule())
            .playerDetailRepoModule(PlayerDetailRepoModule())
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, playerDetailsViewModelFactory).get(PlayerDetailsViewModel::class.java)

        val playerDetails : MutableLiveData<PlayersDetailsModel>? = viewModel.onShowPlayerInfo()
        val displayProgress : MutableLiveData<Boolean>? = viewModel.getShowProgress()
        val playerFrmDB : MutableLiveData<Players>? = viewModel.onShowDBPlayer()
        val showDBGetProgress : MutableLiveData<Boolean>? = viewModel.getShowDBGetSuccess()
        val showDBAddSuccess: MutableLiveData<Boolean>? = viewModel.getShowDBAddSuccess()
        var playerId = arguments?.getString("message")
        var playerIdInt: Int = Integer.parseInt(playerId!!)


        // Call from DB
        viewModel.getPlayerFromDB(playerIdInt)

        playerFrmDB?.observe(this, object: Observer<Players>{
            override fun onChanged(t: Players?) {
                Log.i("PlayerDtFragmentDB", "${t!!.strPlayer}")

                tv_description.text = t.strDescriptionEN
                img_view.loadImage(t.strCutout)
                tv_name.text = t.strPlayer
                tv_team.text = t.strTeam
            }
        })
        // End of Call to DB

        // Call to Network
  //      viewModel.getOnePlayerInfo(playerId!!)

        displayProgress?.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t == false)
                    prgBar.visibility = View.GONE
                else
                    prgBar.visibility = View.VISIBLE
            }
        })

        playerDetails?.observe(this, object: Observer<PlayersDetailsModel> {
            override fun onChanged(t: PlayersDetailsModel?) {
                Log.i("PlayerDtFragmentNW", "${t!!.players[0].strPlayer}")

                tv_description.text = t.players[0].strDescriptionEN
                img_view.loadImage(t.players[0].strCutout)
                tv_name.text = t.players[0].strPlayer
                tv_team.text = t.players[0].strTeam

            }
        })
                // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false)
    }


}
