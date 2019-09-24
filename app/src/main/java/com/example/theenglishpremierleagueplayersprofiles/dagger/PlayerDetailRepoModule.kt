package com.example.theenglishpremierleagueplayersprofiles.dagger

import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerDetailsRepository
import com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview.PlayerDetailsViewModelFactory
import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerModelViewFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerDetailRepoModule {

    @Provides
    @Singleton
    fun providePlayerDetailViewModelFactory(playerDetailRepo: PlayerDetailsRepository): PlayerDetailsViewModelFactory {
        return PlayerDetailsViewModelFactory(playerDetailRepo)
    }
}