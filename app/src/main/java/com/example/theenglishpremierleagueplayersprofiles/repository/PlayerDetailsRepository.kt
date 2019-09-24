package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.common.Constants
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Observable
import javax.inject.Inject

class PlayerDetailsRepository @Inject constructor(val clientInterface: ClientInterface, application: Application): Application(){

    lateinit var playerDetails: Observable<PlayersDetailsModel>

    fun getAPlayersDetail(onePlayerId: String): Observable<PlayersDetailsModel> {
        playerDetails = clientInterface.getAPlayerRecord(onePlayerId)
        return playerDetails
    }
}