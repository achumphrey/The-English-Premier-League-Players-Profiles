package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.inflate
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.model.playerdetails.Players
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.PlayersListModel
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import kotlinx.android.synthetic.main.cardview_players.view.*
import kotlinx.android.synthetic.main.cardview_recyclerview.view.*
import kotlinx.android.synthetic.main.cardview_recyclerview.view.iv_team_logo

class PlayersAdapter (private val players: PlayersListModel, private val listener: OnPlayerClickListener):
    RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {

        val view: View = parent.inflate(R.layout.cardview_players, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return players.player.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {

        holder.tvPlayerName.text = players.player[position].strPlayer
        holder.tvDateBorn.text = players.player[position].dateBorn
        holder.tvNationality.text = players.player[position].strNationality
        holder.tvHeight.text = players.player[position].strHeight
        holder.tvPosition.text = players.player[position].strPosition
        holder.tvWage.text = players.player[position].strWage

        holder.imgView.loadImage(players.player[position].strCutout)

        holder.bind(players.player[position], listener)
    }

}
class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(players: Player, listener: OnPlayerClickListener) {
        itemView.setOnClickListener {
            listener.onPlayerClick(players)
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

interface OnPlayerClickListener {

    fun onPlayerClick(players: Player) {
    }
}
