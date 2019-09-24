package com.example.theenglishpremierleagueplayersprofiles.model.teamlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "team_table")
data class Teams (

	@SerializedName("idTeam")
	@PrimaryKey
	val idTeam : Int,
	@SerializedName("strTeam") val strTeam : String,
	@SerializedName("intFormedYear") val intFormedYear : Int,
	@SerializedName("strStadium") val strStadium : String,
	@SerializedName("strStadiumLocation") val strStadiumLocation : String,
	@SerializedName("intStadiumCapacity") val intStadiumCapacity : Int,
	@SerializedName("strDescriptionEN") val strDescriptionEN : String,
	@SerializedName("strTeamBadge") val strTeamBadge : String

)