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
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
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
        var playerId = arguments?.getString("message")


        // Call to Network
        viewModel.getOnePlayerInfo(playerId!!)

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
                Log.i("TeamFragment", "${t!!.players[0].strPlayer}")

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
