package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.surveyData.ListSurvey

class SurveyAdapter(val list: List<ListSurvey>, val surveyClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_survey, parent, false)
    )

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): Int = list[position].id_kuisioner

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position], surveyClickListener)

    inner class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        private var title: TextView = view.findViewById(R.id.title)
        private var subtitle: TextView = view.findViewById(R.id.description)

        fun bind(model: ListSurvey, surveyClickListener: (Int) -> Unit) {
            title.text = model.judul
            subtitle.text = model.deskripsi
            view.setOnClickListener {
                val data: Int = getItem(adapterPosition)
                surveyClickListener(data)
                notifyDataSetChanged()
            }
        }

    }
}