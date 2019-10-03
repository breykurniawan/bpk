package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.surveyData.ListSurvey
import com.sis.app.models.surveyData.RespondentSurveyData

class RespondentDataAdapter(val list: List<RespondentSurveyData>?) :
    RecyclerView.Adapter<RespondentDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_respondent, parent, false)
    )

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list?.get(position))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var name: TextView = view.findViewById(R.id.name)
        private var stakeholder: TextView = view.findViewById(R.id.stakeholder)
        private var date: TextView = view.findViewById(R.id.date_created)

        fun bind(model: RespondentSurveyData?) {
            name.text = model?.nama_responden
            stakeholder.text = model?.pemangku_kepentingan
            date.text = model?.created_at
        }

    }
}