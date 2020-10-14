package com.example.theenglishpremierleagueplayersprofiles.view.playersview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.model.playerlist.Player
import kotlinx.android.synthetic.main.cardview_players.view.*

class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(players: Player, listener: OnPlayerClickListener) {

            itemView.tv_player_name.text = players.strPlayer
            itemView.iv_player_logo.loadImage(players.strCutout)
            itemView.tv_date_born.text = players.dateBorn
            itemView.tv_nationality.text = players.strNationality
            itemView.tv_height.text = players.strHeight
            itemView.tv_wage.text = players.strWage
            itemView.tv_position.text = players.strPosition
            itemView.setOnClickListener {
                listener.onPlayerClick(players)
            }
        }
}