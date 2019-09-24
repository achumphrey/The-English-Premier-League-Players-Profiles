package com.example.theenglishpremierleagueplayersprofiles.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams

@Database(entities = arrayOf(Player::class), version = 1, exportSchema = false)
abstract class PlayerDatabase : RoomDatabase() {

    abstract fun playerDao():PlayerDao

    companion object{

        @Volatile
        private var INSTANCE:PlayerDatabase? = null

        fun getDatabase(context: Context):PlayerDatabase?{
            val tempInstance = INSTANCE
            if (INSTANCE!=null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    PlayerDatabase::class.java,"player_database")
                    .build()

                INSTANCE = instance
            }
            return INSTANCE
        }
    }

}