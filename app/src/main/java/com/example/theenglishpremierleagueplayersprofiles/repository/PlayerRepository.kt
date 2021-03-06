package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.database.PlayerDao
import com.example.theenglishpremierleagueplayersprofiles.database.TeamDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class PlayerRepository @Inject constructor(val clientInterface: ClientInterface, application: Application): Application() {

    var playerDao: PlayerDao? = TeamDatabase.getDatabase(application)?.playerDao()
//    lateinit var playerList: Observable<PlayersListModel>
    lateinit var teamPlayersFrmDB: Flowable<List<Player>>

    // for network call
    fun getPlayersList(teamId: String?): Observable<PlayersListModel>{
        return clientInterface.getAllPlayersRecord(teamId)
    }

    fun addPlayersToDatabase(players: List<Player>): Completable {
        return  playerDao!!.insertPlayers(players)
    }

    fun getDBTeamPlayers(idTeam: Int): Flowable<List<Player>>{
        teamPlayersFrmDB = playerDao!!.getDBPlayersByTeamId(idTeam)
        return teamPlayersFrmDB
    }
}