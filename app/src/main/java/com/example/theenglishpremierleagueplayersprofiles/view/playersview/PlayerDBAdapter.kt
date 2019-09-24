package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.inflate
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import kotlinx.android.synthetic.main.cardviewdb_players.view.*


class PlayerDBAdapter (private val players: List<Player>, private val listener: OnPlayerDBClickListener):
    RecyclerView.Adapter<PlayerDBViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerDBViewHolder {

        val view: View = parent.inflate(R.layout.cardviewdb_players, false)
        return PlayerDBViewHolder(view)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: PlayerDBViewHolder, position: Int) {

        holder.tvPlayerName.text = players[position].strPlayer
        holder.tvDateBorn.text = players[position].dateBorn
        holder.tvNationality.text = players[position].strNationality
        holder.tvHeight.text = players[position].strHeight
        holder.tvPosition.text = players[position].strPosition
        holder.tvWage.text = players[position].strWage

        holder.imgView.loadImage(players[position].strCutout)

        holder.bind(players[position], listener)
    }

}
class PlayerDBViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(players: Player, listener: OnPlayerDBClickListener) {
        itemView.setOnClickListener {
            listener.onPlayerDBClick(players)
        }
    }

    val tvPlayerName = view.tv_player_name
    val imgView = view.iv_team_logo
    val tvDateBorn = view.tv_date_born
    val tvNationality = view.tv_nationality
    val tvPosition = view.tv_position
    val tvHeight = view.tv_height
    val tvWage = view.tv_wage
}

interface OnPlayerDBClickListener {

    fun onPlayerDBClick(players: Player)
}
