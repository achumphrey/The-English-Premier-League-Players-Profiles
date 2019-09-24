package com.example.theenglishpremierleagueplayersprofiles.model.playerlist

import com.google.gson.annotations.SerializedName

data class PlayersListModel (

	@SerializedName("player") val player : List<Player>
)