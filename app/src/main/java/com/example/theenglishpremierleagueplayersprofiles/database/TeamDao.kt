package com.example.theenglishpremierleagueplayersprofiles.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(teams: List<Teams>): Completable

    @Query("Select * from team_table")
    fun getAllTeams(): Flowable<List<Teams>>

    @Query("select * from team_table where idTeam = :idTeam")
    fun getTeamById(idTeam:Int): Flowable<Teams>


}