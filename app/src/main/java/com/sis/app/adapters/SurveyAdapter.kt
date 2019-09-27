package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.SurveyDesc

class SurveyAdapter(val list: List<SurveyDesc>, val surveyClickListener: (SurveyDesc) -> Unit) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_survey, parent, false)
    )

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): SurveyDesc = list[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position], surveyClickListener)

    inner class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        private lateinit var title: TextView
        private lateinit var subtitle: TextView

        init {
            title = view.findViewById(R.id.title)
            subtitle = view.findViewById(R.id.subtitle)
        }

        fun bind(model: SurveyDesc, surveyClickListener: (SurveyDesc) -> Unit) {
            title.text = model.title
            subtitle.text = model.description
            view.setOnClickListener {
                val model: SurveyDesc = getItem(adapterPosition)
                surveyClickListener(model)
                notifyDataSetChanged()
            }
        }

    }
}