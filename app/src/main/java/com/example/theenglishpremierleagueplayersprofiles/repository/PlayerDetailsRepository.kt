package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.database.PlayerDetailsDao
import com.example.theenglishpremierleagueplayersprofiles.database.TeamDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class PlayerDetailsRepository @Inject constructor(val clientInterface: ClientInterface, application: Application): Application(){

    var playerDetailsDao: PlayerDetailsDao? = TeamDatabase.getDatabase(application)?.playerDetailsDao()
    lateinit var playerDetails: Observable<PlayersDetailsModel>
    lateinit var playerFrmDB: Flowable<Players>


    // Call to Network
    fun getAPlayersDetail(onePlayerId: String): Observable<PlayersDetailsModel> {
        playerDetails = clientInterface.getAPlayerRecord(onePlayerId)
        return playerDetails
    }

    fun addPlayerToDatabase(players: List<Players>): Completable {
        return  playerDetailsDao!!.insertPlayers(players)
    }

    fun getAPlayerFrmDB(idPlayer: Int): Flowable<Players> {
        playerFrmDB = playerDetailsDao!!.getPlayerById(idPlayer)
        return playerFrmDB
    }

}