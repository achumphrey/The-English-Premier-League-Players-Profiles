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

class TeamDBAdapter (private val teams: List<Teams>, private val listener: OnTeamDBClickLister):
    RecyclerView.Adapter<TeamDBViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamDBViewHolder {

        val view: View = parent.inflate(R.layout.cardview_recyclerview, false)
        return TeamDBViewHolder(view)
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    override fun onBindViewHolder(holder: TeamDBViewHolder, position: Int) {

        holder.tvTeamName.text = teams[position].strTeam
        holder.tvYeraFormed.text = "" + teams[position].intFormedYear
        holder.tvStadiumLocation.text = teams[position].strStadiumLocation
        holder.tvStadiumName.text = teams[position].strStadium
        holder.tvStadiumCapacity.text = "" + teams[position].intStadiumCapacity

        holder.imgView.loadImage(teams[position].strTeamBadge)
        holder.bind(teams[position], listener)
    }

}
class TeamDBViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(teams: Teams, listener: OnTeamDBClickLister) {
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

interface OnTeamDBClickLister {

    fun onTeamClick(teams: Teams)
}


