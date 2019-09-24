package com.example.theenglishpremierleagueplayersprofiles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.view.playerdetailsview.PlayersDetailsFragment
import com.example.theenglishpremierleagueplayersprofiles.view.playersview.PlayerFragment
import com.example.theenglishpremierleagueplayersprofiles.view.teamview.TeamFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment()
    }

    fun addFragment(){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.frm_container, TeamFragment())
            //      .addToBackStack(null)
            .commit()
    }
}
