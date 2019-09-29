package com.sis.app.adapters

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.sis.app.R
//import com.sis.app.models.surveyData.Subs
//
//class SubsAdapter(val list: List<Subs>?) : RecyclerView.Adapter<SubsAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.list_item_pertanyaan_bagian, parent, false)
//    )
//
//    override fun getItemCount(): Int = list?.size ?: 0
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(list?.get(position))
//    }
//
//    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
//        private var title: TextView = view.findViewById(R.id.title)
//        private var rvListBagian: RecyclerView = view.findViewById(R.id.rv_list_bagian)
//
//        fun bind(model: Subs?) {
//            title.text = model?.judul
//            val childAdapter = QuestionListAdapter(model?.pertanyaan)
//
//            rvListBagian.apply {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(view.context)
//                adapter = childAdapter
//            }
//        }
//    }
//}