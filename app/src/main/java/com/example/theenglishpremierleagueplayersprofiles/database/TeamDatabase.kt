package com.example.theenglishpremierleagueplayersprofiles.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams

@Database(entities = arrayOf(Teams::class, Player::class), version = 1, exportSchema = false)
abstract class TeamDatabase : RoomDatabase() {

    abstract fun teamDao():TeamDao

    companion object{

        @Volatile
        private var INSTANCE:TeamDatabase? = null

        fun getDatabase(context: Context):TeamDatabase?{
            val tempInstance = INSTANCE
            if (INSTANCE!=null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    TeamDatabase::class.java,"team_database")
                    .build()

                INSTANCE = instance
            }
            return INSTANCE
        }
    }


}