package com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerDetailsRepository
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlayerDetailsViewModel (val playerDetailRepo: PlayerDetailsRepository) : ViewModel()  {

    private var playersDetail: MutableLiveData<PlayersDetailsModel>? = MutableLiveData()
    private var showProgress: MutableLiveData<Boolean>? = MutableLiveData()
    var compositeDisposable = CompositeDisposable()
    lateinit var disposable: Disposable
    private var showDBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    private var showDBGetSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    var playerFromDb: MutableLiveData<Players>? = MutableLiveData()

    fun getShowProgress(): MutableLiveData<Boolean>?{
        return showProgress
    }

    fun getShowDBGetSuccess(): MutableLiveData<Boolean>?{
        return showDBGetSuccess
    }

    fun getShowDBAddSuccess(): MutableLiveData<Boolean>?{
        return showDBAddSuccess
    }

    // get a player from database
    fun getPlayerFromDB(idPlayer: Int) {
        showProgress?.value = true

        val playerFrmDB: Flowable<Players> = playerDetailRepo.getAPlayerFrmDB(idPlayer)
        compositeDisposable.add(
        playerFrmDB
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({t -> createTeamPlayer(t)
                showDBGetSuccess?.value = true
                showProgress?.value = false },{showDBGetSuccess?.postValue(false)})
        )
    }

    fun onShowDBPlayer() : MutableLiveData<Players>?{
        return playerFromDb
    }

    private fun createTeamPlayer(playerDB: Players) {
        Log.i(TAGPDVMDB, "${playerDB.strPlayer}")
        playerFromDb?.value = playerDB
    }

    //End of DB

    // Call to Network
    fun getOnePlayerInfo(playerId:String){
        showProgress?.value = true

        val onePlayerObservable: Observable<PlayersDetailsModel> = playerDetailRepo.getAPlayersDetail(playerId)

        compositeDisposable.add(
        onePlayerObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //    .subscribe(teamObserver())
            .subscribe({t-> createPlayerInfo(t)
                showProgress?.value = false})
        )
    }

    fun onShowPlayerInfo() : MutableLiveData<PlayersDetailsModel>?{
        return playersDetail
    }

    private fun createPlayerInfo(player: PlayersDetailsModel) {
        Log.i(TAGPDVMNW, "${player.players[0].strPlayer}")
        playersDetail?.value = player

        addPlayerToDB(player)
    }

    // Add to DB
    fun addPlayerToDB(player: PlayersDetailsModel){
        compositeDisposable.add(
            playerDetailRepo.addPlayerToDatabase(player.players)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({showDBAddSuccess?.value = true},{
                    Log.i("PlayerDtViewModel error",it.message)
                    showDBAddSuccess?.value=false})
        )
    }

    private fun playersObserver(): Observer<PlayersDetailsModel> {

        return object : Observer<PlayersDetailsModel> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: PlayersDetailsModel) {
                showProgress?.value = false
                createPlayerInfo(t)
            }

            override fun onError(e: Throwable) {
                Log.i(TAGPDVM, "something went wrong")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAGPDVM, "ViewModel destroyed")
    }

    fun showError(){
        Log.i(ERROR, "Something Happened")
    }

    fun onDestroy() {
        disposable.dispose()
        compositeDisposable.clear()
    }

    companion object{
        const val TAGPDVM = "PlayerDetailsViewModel"
        const val TAGPDVMNW = "PlayerDetailViewModelNW"
        const val TAGPDVMDB = "PlayerDtViewModelDBGet"
        const val ERROR = "SHOW_ERROR"
    }
}