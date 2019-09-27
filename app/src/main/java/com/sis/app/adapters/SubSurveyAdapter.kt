package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.SubSurvey
import com.sis.app.models.Survey

class SubSurveyAdapter(val list: List<SubSurvey>?, val subSurveyClickListener: (SubSurvey?) -> Unit) :
    RecyclerView.Adapter<SubSurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_survey, parent, false)
    )

    override fun getItemCount(): Int = list?.size ?: 0

    fun getItem(position: Int): SubSurvey? = list?.get(position)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list?.get(position), subSurveyClickListener)

    inner class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView = view.findViewById(R.id.title)
        private var subtitle: TextView = view.findViewById(R.id.description)

        fun bind(model: SubSurvey?, subSurveyClickListener: (SubSurvey) -> Unit) {
            title.text = model?.title
            subtitle.text = model?.description
            view.setOnClickListener {
                val data: SubSurvey? = getItem(adapterPosition)
                subSurveyClickListener(data)
                notifyDataSetChanged()
            }
        }

    }
}