package com.sis.app.adapters

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.sis.app.R
//import com.sis.app.models.surveyData.SubSurvey
//
//class SubSurveyAdapter(val list: List<SubSurvey>?, val subSurveyClickListener: (List<SubSurvey>?) -> Unit) :
//    RecyclerView.Adapter<SubSurveyAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.list_item_survey, parent, false)
//    )
//
//    override fun getItemCount(): Int = list?.size ?: 0
//
//    fun getItem(position: Int): SubSurvey? = list?.get(position)
//
//    fun getSurveyList(): MutableList<SubSurvey>? = list as MutableList<SubSurvey>
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
//        holder.bind(list?.get(position), subSurveyClickListener)
//
//    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//        private var title: TextView = view.findViewById(R.id.title)
//        private var subtitle: TextView = view.findViewById(R.id.description)
//
//        fun bind(model: SubSurvey?, subSurveyClickListener: (List<SubSurvey>?) -> Unit) {
//            title.text = model?.judul
////            subtitle.text = model?.
//            view.setOnClickListener {
////                val data: SubSurvey? = getItem(adapterPosition)
////                val data: List<SubSurvey>? = getSurveyList().remove
////                subSurveyClickListener(data)
//                notifyDataSetChanged()
//            }
//        }
//
//    }
//}