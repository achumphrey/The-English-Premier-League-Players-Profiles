package com.example.theenglishpremierleagueplayersprofiles.model.teamlist

import com.google.gson.annotations.SerializedName

data class TeamsModel (

	@SerializedName("teams") val teams : List<Teams>
)