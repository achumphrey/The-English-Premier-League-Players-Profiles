package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.inflate
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player

class PlayersAdapter (private val players: MutableList<Player>, private val listener: OnPlayerClickListener):
    RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {

        val view: View = parent.inflate(R.layout.cardview_players, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position], listener)
    }

    fun updatePlayerList(newPlayerList: List<Player>){
        players.clear()
        players.addAll(newPlayerList)
        notifyDataSetChanged()
    }
}