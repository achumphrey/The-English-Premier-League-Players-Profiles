package com.example.theenglishpremierleagueplayersprofiles.dagger

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import com.example.theenglishpremierleagueplayersprofiles.repository.TeamRepository
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamModelViewFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTeamViewModelFactory(teamRepository: TeamRepository): TeamModelViewFactory{
        return TeamModelViewFactory(teamRepository)
    }
}