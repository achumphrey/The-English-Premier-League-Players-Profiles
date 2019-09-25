package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import com.example.theenglishpremierleagueplayersprofiles.repository.TeamRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TeamViewModel(val teamRepository: TeamRepository) : ViewModel() {

    private var teamList: MutableLiveData<TeamsModel>? = MutableLiveData()
    private var showProgress: MutableLiveData<Boolean>? = MutableLiveData()
    var compositeDisposable = CompositeDisposable()
    lateinit var disposable: Disposable
    private var showDBGetSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    private var showDBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    var teamsFromDb: MutableLiveData<List<Teams>>? = MutableLiveData()


    fun getShowProgress():MutableLiveData<Boolean>?{
        return showProgress
    }

    fun getShowDBGetSuccess(): MutableLiveData<Boolean>?{
        return showDBGetSuccess
    }

    fun getShowDBAddSuccess(): MutableLiveData<Boolean>?{
        return showDBAddSuccess
    }

    // get teams from database
    fun getTeamsFromDB(){
        showProgress?.value = true

        val teamsFrmDB: Flowable<List<Teams>> = teamRepository.getTeamsFrmDB()
        compositeDisposable.add(
        teamsFrmDB
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({t -> teamsFromDb?.postValue(t)
                showDBGetSuccess?.value = true
                showProgress?.value = false },{showDBGetSuccess?.postValue(false)}))
    }

    fun onShowDBTeam() : MutableLiveData<List<Teams>>?{
        return teamsFromDb
    }

    private fun makeDBTeam(teamsDB: List<Teams>) {
        Log.i(TAGTVMDB, "${teamsDB[0].strTeam}")
        teamsFromDb?.value = teamsDB
    }
    //end of database call

    //Retrofit call
    fun getTeamRecords(){
        showProgress?.value = true

        val teamModelObservable: Observable<TeamsModel> = teamRepository.getTeamRecords()

        compositeDisposable.add(
        teamModelObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //    .subscribe(teamObserver())
            .subscribe({t-> makeTeam(t)
                showProgress?.value = false}))
    }


    fun onShowTeamList() : MutableLiveData<TeamsModel>?{
        return teamList
    }

    private fun makeTeam(teamsList: TeamsModel) {
        Log.i(TAGTVMNW, "${teamsList.teams[1].strTeam}")
        teamList?.value = teamsList

        addTeamToDB(teamsList)
    }

    fun addTeamToDB(teamsList: TeamsModel){
        compositeDisposable.add(
            teamRepository.addTeamsToDatabase(teamsList.teams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({showDBAddSuccess?.value = true},{
                    Log.i("ViewModel error",it.message)
                    showDBAddSuccess?.value=false})
        )
    }

    private fun teamObserver(): Observer<TeamsModel>{

        return object : Observer<TeamsModel>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: TeamsModel) {
                showProgress?.value = false
                makeTeam(t)

            }

            override fun onError(e: Throwable) {
                Log.i(TAGTVM, "something went wrong")
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAGTVM, "ViewModel destroyed")
    }

    fun showError(){
        Log.i("SHOW_ERROR", "Something Happened")
    }

    fun onDestroy() {
        disposable.dispose()
        compositeDisposable.clear()
    }

    companion object{
        const val TAGTVM = "TeamViewModel"
        const val TAGTVMNW = "TeamViewModelNW"
        const val TAGTVMDB = "TeamViewModelDB"
    }

}
