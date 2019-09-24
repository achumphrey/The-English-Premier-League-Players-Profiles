package com.example.theenglishpremierleagueplayersprofiles.model.playerdetails

import com.google.gson.annotations.SerializedName

data class PlayersDetailsModel (

	@SerializedName("players") val players : List<Players>
)