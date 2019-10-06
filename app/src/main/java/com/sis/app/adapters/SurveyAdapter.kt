package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.surveyData.ListSurvey

class SurveyAdapter(val list: List<ListSurvey>?, val surveyClickListener: (Int?, Int?) -> Unit) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_survey, parent, false)
    )

    override fun getItemCount(): Int = list?.size ?:0

    fun getItem(position: Int): Int? = list?.get(position)?.id_kuisioner

    fun getIdTipe(position: Int): Int? = list?.get(position)?.tipe_responden

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list?.get(position), surveyClickListener, position)

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var title: TextView = view.findViewById(R.id.title)
        private var subtitle: TextView = view.findViewById(R.id.description)
        private var number: TextView = view.findViewById(R.id.number)

        fun bind(model: ListSurvey?, surveyClickListener: (Int?, Int?) -> Unit, pos: Int) {
            title.text = model?.judul
            subtitle.text = model?.nama_tipe
            number.text = "${pos+1}."
            view.setOnClickListener {
                val data: Int? = getItem(adapterPosition)
                val tipe: Int? = getIdTipe(adapterPosition)
                surveyClickListener(data, tipe)
                notifyDataSetChanged()
            }
        }

    }
}