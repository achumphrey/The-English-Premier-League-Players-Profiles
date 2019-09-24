package com.example.theenglishpremierleagueplayersprofiles.repository

import android.app.Application
import com.example.theenglishpremierleagueplayersprofiles.common.Constants
import com.example.theenglishpremierleagueplayersprofiles.database.PlayerDao
import com.example.theenglishpremierleagueplayersprofiles.database.TeamDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.network.ClientInterface
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class TeamRepository @Inject constructor(val clientInterface: ClientInterface, application: Application): Application() {

    var teamDao = TeamDatabase.getDatabase(application)?.teamDao()
    lateinit var teamRecord: Observable<TeamsModel>
    lateinit var teams: Flowable<List<Teams>>



    fun getTeamRecords(): Observable<TeamsModel>{
        teamRecord = clientInterface.getAllTeamsRecord(Constants.LEAGUE_ID)
        return teamRecord
    }

    fun addTeamsToDatabase(teams:List<Teams>):Completable{
      return  teamDao!!.insertTeams(teams)
    }

    fun getTeamsFrmDB(): Flowable<List<Teams>>{
        teams = teamDao!!.getAllTeams()
        return teams
    }
}