package com.example.theenglishpremierleagueplayersprofiles.view.teamview


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theenglishpremierleagueplayersprofiles.repository.TeamRepository

class TeamModelViewFactory(private val teamRepository: TeamRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamViewModel(teamRepository) as T
    }


}