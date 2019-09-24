package com.example.theenglishpremierleagueplayersprofiles.dagger

import android.app.Application
import android.content.Context

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

       /* DaggerCakeViewAPPComponent.builder()
            .networkModule(NetworkModule(this))
            .build()
            .inject(this)*/
    }

}