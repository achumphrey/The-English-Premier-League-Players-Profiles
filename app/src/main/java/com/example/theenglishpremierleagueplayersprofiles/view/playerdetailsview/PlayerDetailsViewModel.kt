package com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.PlayersDetailsModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerDetailsRepository
import com.example.theenglishpremierleagueplayersprofiles.repository.PlayerRepository
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

    fun getShowProgress(): MutableLiveData<Boolean>?{
        return showProgress
    }

    fun getOnePlayerInfo(playerId:String){
        showProgress?.value = true

        val onePlayerObservable: Observable<PlayersDetailsModel> = playerDetailRepo.getAPlayersDetail(playerId)

        onePlayerObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //    .subscribe(teamObserver())
            .subscribe({t-> createPlayerInfo(t)
                showProgress?.value = false})
    }

    fun onShowPlayerInfo() : MutableLiveData<PlayersDetailsModel>?{
        return playersDetail
    }

    private fun createPlayerInfo(player: PlayersDetailsModel) {
        Log.i("TeamViewModel", "${player.players[0].strPlayer}")
        playersDetail?.value = player
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