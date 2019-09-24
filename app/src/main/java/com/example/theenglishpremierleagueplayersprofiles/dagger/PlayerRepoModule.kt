package com.example.theenglishpremierleagueplayersprofiles.dagger

import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerModelViewFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerRepoModule {

    @Provides
    @Singleton
    fun providePlayerViewModelFactory(playerRepo:PlayerRepository): PlayerModelViewFactory {
        return PlayerModelViewFactory(playerRepo)
    }
}