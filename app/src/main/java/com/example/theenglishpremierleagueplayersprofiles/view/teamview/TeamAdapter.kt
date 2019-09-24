package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.inflate
import com.example.theenglishpremierleagueplayersprofiles.common.loadImage
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.TeamsModel
import kotlinx.android.synthetic.main.cardview_recyclerview.view.*

class TeamAdapter (private val teamsModel: TeamsModel, private val listener: OnTeamClickLister):
    RecyclerView.Adapter<TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {

        val view: View = parent.inflate(R.layout.cardview_recyclerview, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount(): Int {
        return teamsModel.teams.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {

        holder.tvTeamName.text = teamsModel.teams[position].strTeam
        holder.tvYeraFormed.text = "" + teamsModel.teams[position].intFormedYear
        holder.tvStadiumLocation.text = teamsModel.teams[position].strStadiumLocation
        holder.tvStadiumName.text = teamsModel.teams[position].strStadium
        holder.tvStadiumCapacity.text = "" + teamsModel.teams[position].intStadiumCapacity

        holder.imgView.loadImage(teamsModel.teams[position].strTeamBadge)
        holder.bind(teamsModel.teams[position], listener)
    }

}
        class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(teams: Teams, listener: OnTeamClickLister) {
                itemView.setOnClickListener {
                    listener.onTeamClick(teams)
                }
            }

            val tvTeamName = view.tv_team_name
            val imgView = view.iv_team_logo
            val tvYeraFormed = view.tv_year_formed
            val tvStadiumLocation = view.tv_stadium_location
            val tvStadiumName = view.tv_stadium
            val tvStadiumCapacity = view.tv_stadium_capacity
        }

        interface OnTeamClickLister {

            fun onTeamClick(teams: Teams)
        }


