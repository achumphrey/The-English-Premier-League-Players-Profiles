package com.example.theenglishpremierleagueplayersprofiles.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PlayerDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlayers(players: List<Players>): Completable

    @Query("select * from players_table where idPlayer = :idPlayer")
    fun getPlayerById(idPlayer: Int): Flowable<Players>

}