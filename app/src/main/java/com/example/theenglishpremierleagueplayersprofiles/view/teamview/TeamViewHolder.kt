package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import kotlinx.android.synthetic.main.cardview_recyclerview.view.*

class TeamViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(teams: Teams, listener: OnteamClickListener) {

            itemView.tv_team_name.text = teams.strTeam
            itemView.tv_year_formed.text = teams.intFormedYear.toString()
            itemView.tv_stadium_location.text = teams.strStadiumLocation
            itemView.tv_stadium.text = teams.strStadium
            itemView.tv_stadium_capacity.text = teams.intStadiumCapacity.toString()
            itemView.iv_team_logo.loadImage(teams.strTeamBadge)
            itemView.setOnClickListener {
                listener.onTeamClick(teams)
            }
        }
}