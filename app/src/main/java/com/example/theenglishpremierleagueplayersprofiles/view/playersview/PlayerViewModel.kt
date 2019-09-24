package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
import com.example.theenglishpremierleagueplayersprofiles.repository.TeamRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlayerViewModel (val playerRepo: PlayerRepository) : ViewModel() {

    private var playersList: MutableLiveData<PlayersListModel>? = MutableLiveData()
    private var showProgress: MutableLiveData<Boolean>? = MutableLiveData()
    var compositeDisposable = CompositeDisposable()
    lateinit var disposable: Disposable
    var playersFromDb: MutableLiveData<List<Player>>? = MutableLiveData()
    private var showDBGetSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    private var showDBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()

    fun getShowProgress(): MutableLiveData<Boolean>?{
        return showProgress
    }

    fun getShowDBGetSuccess(): MutableLiveData<Boolean>?{
        return showDBGetSuccess
    }

    fun getShowDBAddSuccess(): MutableLiveData<Boolean>?{
        return showDBAddSuccess
    }

    // get teams from database
    fun getTeamPlayersFromDB(teamId:Int) {
        showProgress?.value = true

        val playersFrmDB: Flowable<List<Player>> = playerRepo.getDBTeamPlayers(teamId)
        playersFrmDB
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({t -> createTeamPlayers(t)
                showDBGetSuccess?.value = true
                showProgress?.value = false },{showDBGetSuccess?.postValue(false)})
    }

    fun onShowDBPlayers() : MutableLiveData<List<Player>>?{
        return playersFromDb
    }

    private fun createTeamPlayers(playersDB: List<Player>) {
        Log.i("PlayerViewModelDBGet", "${playersDB[0].strPlayer}")
        playersFromDb?.value = playersDB
    }

  //End of DB

  // Start of Network Call
    fun getAllTeamPlayers(teamId:String?) {
      showProgress?.value = true


        val allTeamPlayersObservable: Observable<PlayersListModel> = playerRepo.getPlayersList(teamId)

        allTeamPlayersObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //    .subscribe(teamObserver())
            .subscribe({t-> createPlayerList(t)
                showProgress?.value = false})
    }

    fun onShowPlayerList() : MutableLiveData<PlayersListModel>?{
        return playersList
    }

    private fun createPlayerList(players: PlayersListModel) {
        Log.i("PlayerViewModelNW", "${players.player[0].strPlayer}")
        playersList?.value = players

        addPlayersToDB(players)
    }

    // Add players to DB
    fun addPlayersToDB(players: PlayersListModel){
        compositeDisposable.add(
            playerRepo.addPlayersToDatabase(players.player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({showDBAddSuccess?.value = true},{
                    Log.i("ViewModel error",it.message)
                    showDBAddSuccess?.value=false})
        )
    }

    private fun playersObserver(): Observer<PlayersListModel> {

        return object : Observer<PlayersListModel> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: PlayersListModel) {
                showProgress?.value = false
                createPlayerList(t)
            }

            override fun onError(e: Throwable) {
                Log.i("TeamViewModel", "something went wrong")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("TeamViewModel", "ViewModel destroyed")
    }

    fun showError(){
        Log.i("SHOW_ERROR", "Something Happened")
    }

    fun onDestroy() {
        disposable.dispose()
        compositeDisposable.clear()
    }
}
