package com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerDetailsRepository


class PlayerDetailsViewModelFactory (private val playerDetailRepo: PlayerDetailsRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayerDetailsViewModel(playerDetailRepo) as T
    }
}