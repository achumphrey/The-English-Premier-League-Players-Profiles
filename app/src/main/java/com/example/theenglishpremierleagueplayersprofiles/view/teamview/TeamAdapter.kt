package com.example.theenglishpremierleagueplayersprofiles.view.teamview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theenglishpremierleagueplayersprofiles.R
import com.example.theenglishpremierleagueplayersprofiles.common.inflate
import com.example.theenglishpremierleagueplayersprofiles.model.teamlist.Teams

class TeamAdapter (private var teamsList: MutableList<Teams>, private val listener: OnteamClickListener):
    RecyclerView.Adapter<TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view: View = parent.inflate(R.layout.cardview_recyclerview, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount(): Int {
        return teamsList.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teamsList[position], listener)
    }

    fun updateTeamList(newTeamList: List<Teams>){
        teamsList.clear()
        teamsList.addAll(newTeamList)
        notifyDataSetChanged()
    }
}

