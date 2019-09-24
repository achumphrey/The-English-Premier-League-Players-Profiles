package com.example.theenglishpremierleagueplayersprofiles.model.playerdetails

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "players_table")
data class Players (

	@SerializedName("idPlayer")
	@PrimaryKey
	val idPlayer : Int,
	@SerializedName("idTeam") val idTeam : Int,
	@SerializedName("strTeam") val strTeam : String,
	@SerializedName("strPlayer") val strPlayer : String,
	@SerializedName("strDescriptionEN") val strDescriptionEN : String,
	@SerializedName("strCutout") val strCutout : String?
)