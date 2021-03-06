package com.example.theenglishpremierleagueplayersprofiles.model.playerlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "player_table")
data class Player (

	@SerializedName("idPlayer")
	@PrimaryKey
	val idPlayer : Int,
	@SerializedName("idTeam") val idTeam : Int,
	@SerializedName("strTeam") val strTeam : String,
	@SerializedName("strNationality") val strNationality : String,
	@SerializedName("strPlayer") val strPlayer : String,
	@SerializedName("dateBorn") val dateBorn : String,
	@SerializedName("strWage") val strWage : String,
	@SerializedName("strDescriptionEN") val strDescriptionEN : String,
	@SerializedName("strPosition") val strPosition : String,
	@SerializedName("strHeight") val strHeight : String,
	@SerializedName("strCutout") val strCutout : String?
)