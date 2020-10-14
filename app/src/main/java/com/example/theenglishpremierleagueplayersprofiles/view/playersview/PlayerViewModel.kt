package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlayerViewModel (val playerRepo: PlayerRepository) : ViewModel() {

    private var playersList: MutableLiveData<List<Player>>? = MutableLiveData()
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

        compositeDisposable.add(
            playerRepo.getDBTeamPlayers(teamId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({t -> createTeamPlayers(t)
                showDBGetSuccess?.value = true
                showProgress?.value = false },{
                it.printStackTrace()
                showDBGetSuccess?.postValue(false)})
        )
    }

    fun onShowDBPlayers() : MutableLiveData<List<Player>>?{
        return playersFromDb
    }

    private fun createTeamPlayers(playersDB: List<Player>) {
        Log.i(TAGPVMDB, playersDB[0].strPlayer)
        playersFromDb?.value = playersDB
    }

  //End of DB

  // Start of Network Call
    fun getAllTeamPlayers(teamId:String?) {
      showProgress?.value = true

      compositeDisposable.add(
          playerRepo.getPlayersList(teamId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({t-> createPlayerList(t)
                showProgress?.value = false},{
                it.printStackTrace()
            })
      )
    }

    fun onShowPlayerList() : MutableLiveData<List<Player>>?{
        return playersList
    }

    private fun createPlayerList(players: PlayersListModel) {
        Log.i(TAGPVMNW, players.player[0].strPlayer)
        playersList?.value = players.player

        addPlayersToDB(players)
    }

    // Add players to DB
    fun addPlayersToDB(players: PlayersListModel){
        compositeDisposable.add(
            playerRepo.addPlayersToDatabase(players.player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({showDBAddSuccess?.value = true},{
                    it.printStackTrace()
                    Log.i("ViewModel error",it.message)
                    showDBAddSuccess?.value=false})
        )
    }

    //never used
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
                Log.i(TAGPVM, "something went wrong")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        Log.i(TAGPVM, "ViewModel destroyed")
    }

    fun showError(){
        Log.i("SHOW_ERROR", "Something Happened")
    }

    fun onDestroy() {
        disposable.dispose()
        compositeDisposable.clear()
    }

    companion object{
        const val TAGPVM = "PlayerViewModel"
        const val TAGPVMNW = "PlayerViewModelNW"
        const val TAGPVMDB = "PlayerViewModelDBGet"
    }
}
