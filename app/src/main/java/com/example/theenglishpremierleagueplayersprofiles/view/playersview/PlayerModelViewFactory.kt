package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
import com.example.theenglishpremierleagueplayersprofiles.repository.TeamRepository
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamViewModel

class PlayerModelViewFactory (private val playerRepo: PlayerRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayerViewModel(playerRepo) as T
    }
}