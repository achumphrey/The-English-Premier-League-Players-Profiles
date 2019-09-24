package com.example.theenglishpremierleagueplayersprofiles.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayers(players: List<Player>): Completable


    @Query("Select * from player_table")
    fun getAllPlayers(): Flowable<List<Player>>

    @Query("select * from player_table where idTeam = :idTeam")
    fun getDBPlayersByTeamId(idTeam: Int): Flowable<List<Player>>


    @Query("select * from player_table where idPlayer = :idPlayer")
    fun getPlayerById(idPlayer: Int): Flowable<Player>


}