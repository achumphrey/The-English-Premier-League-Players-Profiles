package com.example.theenglishpremierleagueplayersprofiles.common

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.theenglishpremierleagueplayersprofiles.dagger.*

class NetworkConnection: Application() {

    companion object {
        private var connection: Boolean = false
        fun checkConnection(): Boolean = connection

        private lateinit var component: NetworkComponent
        fun getComponent() : NetworkComponent = component
    }

    override fun onCreate() {
        super.onCreate()
        component = buildComponent()
        connection = amIConnected()
    }

    private fun amIConnected(): Boolean {
        val connectivityManager = applicationContext?.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network: Network? = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager
                .getNetworkCapabilities(network) //activeNetworkInfo
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun buildComponent(): NetworkComponent {
        return DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(this))
            .playerDetailRepoModule(PlayerDetailRepoModule())
            .repositoryModule(RepositoryModule())
            .playerRepoModule(PlayerRepoModule())
            .build()
    }
}