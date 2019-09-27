package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.Survey
import com.sis.app.models.SubSurvey

class SurveyAdapter(val list: List<Survey>, val surveyClickListener: (List<SubSurvey>) -> Unit) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_survey, parent, false)
    )

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): List<SubSurvey> = list[position].subsurvey

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position], surveyClickListener)

    inner class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView = view.findViewById(R.id.title)
        private var subtitle: TextView = view.findViewById(R.id.description)

        fun bind(model: Survey, surveyClickListener: (List<SubSurvey>) -> Unit) {
            title.text = model.title
            subtitle.text = model.description
            view.setOnClickListener {
                val data: List<SubSurvey> = getItem(adapterPosition)
                surveyClickListener(data)
                notifyDataSetChanged()
            }
        }

    }
}