package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.common.Constants
import com.example.theenglishpremierleagueplayersprofiles.database.PlayerDao
import com.example.theenglishpremierleagueplayersprofiles.database.PlayerDatabase
import com.example.theenglishpremierleagueplayersprofiles.database.TeamDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class PlayerRepository @Inject constructor(val clientInterface: ClientInterface, application: Application): Application() {

    var playerDao: PlayerDao? = PlayerDatabase.getDatabase(application)?.playerDao()
    lateinit var playerList: Observable<PlayersListModel>
    lateinit var playersFrmDB: Flowable<List<Player>>
    lateinit var playerFrmDB: Flowable<Player>
    lateinit var teamPlayersFrmDB: Flowable<List<Player>>

    fun getPlayersList(teamId: String?): Observable<PlayersListModel>{
        playerList = clientInterface.getAllPlayersRecord(teamId)
        return playerList
    }

    fun addPlayersToDatabase(players: List<Player>): Completable {
        return  playerDao!!.insertPlayers(players)
    }

    fun getTeamPlayers(): Flowable<List<Player>>{
        playersFrmDB = playerDao!!.getAllPlayers()
        return playersFrmDB
    }

    fun getDBTeamPlayers(idTeam: Int): Flowable<List<Player>>{
        teamPlayersFrmDB = playerDao!!.getDBPlayersByTeamId(idTeam)
        return teamPlayersFrmDB
    }

    fun getAPlayerFrmDB(idPlayer: Int): Flowable<Player> {
        playerFrmDB = playerDao!!.getPlayerById(idPlayer)
        return playerFrmDB
    }



}