package com.example.theenglishpremierleagueplayersprofiles.network



import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ClientInterface {

    @GET("lookup_all_teams.php")
    fun getAllTeamsRecord(@Query("id") leagueId : String): Observable<TeamsModel>

    @GET("lookup_all_players.php")
    fun getAllPlayersRecord(@Query("id") teamId : String?): Observable<PlayersListModel>

    @GET("lookupplayer.php")
    fun getAPlayerRecord(@Query("id") playerId : String): Observable<PlayersDetailsModel>
}

