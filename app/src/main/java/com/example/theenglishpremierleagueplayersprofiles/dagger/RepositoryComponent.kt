package com.example.theenglishpremierleagueplayersprofiles.dagger


import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerFragment
import com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview.PlayersDetailsFragment
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    RepositoryModule::class,
    PlayerRepoModule::class,
    PlayerDetailRepoModule::class])
interface NetworkComponent {

    fun inject(teamFragment: TeamFragment)

    fun inject (playerFragment: PlayerFragment)

    fun inject (playersDetailsFragment: PlayersDetailsFragment)
}