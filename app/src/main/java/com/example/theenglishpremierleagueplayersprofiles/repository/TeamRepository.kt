package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.common.Constants
import com.example.theenglishpremierleagueplayersprofiles.database.TeamDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class TeamRepository @Inject constructor(private val clientInterface: ClientInterface, application: Application)
    : Application() {

    var teamDao = TeamDatabase.getDatabase(application)?.teamDao()
    lateinit var teams: Flowable<List<Teams>>

    fun getTeamRecords(): Observable<TeamsModel>{
        return clientInterface.getAllTeamsRecord(Constants.LEAGUE_ID)

    }

    fun addTeamsToDatabase(teams:List<Teams>):Completable{
      return  teamDao!!.insertTeams(teams)
    }

    fun getTeamsFrmDB(): Flowable<List<Teams>>{
        teams = teamDao!!.getAllTeams()
        return teams
    }
}