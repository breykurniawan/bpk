package com.survey.app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.survey.app.R
import com.survey.app.models.CheckboxModel
import com.survey.app.models.Question
import com.survey.app.others.Utility

class QuestionListAdapter(val list: List<Question>) :
    RecyclerView.Adapter<QuestionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListAdapter.ViewHolder {
        return when (viewType) {
            Utility.QUESTION_TYPE_TEXT -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_text,
                    parent,
                    false
                ), viewType
            )
            Utility.QUESTION_TYPE_TEXTAREA -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_textarea,
                    parent,
                    false
                ), viewType
            )
            Utility.QUESTION_TYPE_CHECKBOX -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_checkbox,
                    parent,
                    false
                ), viewType
            )
            Utility.QUESTION_TYPE_RADIO -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_radio,
                    parent,
                    false
                ), viewType
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_empty, parent, false),
                viewType
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].question_type) {
            "text " -> Utility.QUESTION_TYPE_TEXT
            "textarea" -> Utility.QUESTION_TYPE_TEXTAREA
            "checkbox" -> Utility.QUESTION_TYPE_CHECKBOX
            "radio" -> Utility.QUESTION_TYPE_RADIO
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QuestionListAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView? = null
        private var option: ListView? = null

        init {
            title = itemView.findViewById(R.id.title)
            when (viewType) {
                /*
                Utility.QUESTION_TYPE_TEXT -> null
                Utility.QUESTION_TYPE_TEXTAREA -> null
                */
                Utility.QUESTION_TYPE_CHECKBOX -> option = itemView.findViewById(R.id.list_checkbox)
                Utility.QUESTION_TYPE_RADIO -> option = itemView.findViewById(R.id.list_radio)
            }
        }

        fun bind(model: Question) {
            title?.text = model.title
            when (model.question_type) {
                "text " -> null
                "textarea" -> null
                "checkbox" -> {
                    val newList: ArrayList<CheckboxModel> = arrayListOf()
                    model.option_name.forEach {
                        newList.add(CheckboxModel(it))
                    }
//                    val modelList :List<CheckboxModel> = newList
                    option?.apply {
                        adapter = CheckBoxAdapter(itemView.context, newList)
                    }
                }
                "radio" -> {
                    val newList: ArrayList<String> = arrayListOf()
                    model.option_name.forEach {
                        newList.add(it)
                    }
                    option?.apply {
                        adapter = RadioButtonAdapter(itemView.context, newList)
                    }
//                    val newList: MutableList<CheckboxModel> = mutableListOf()
//                    model.option_name.forEach {
//                        newList.add(CheckboxModel(it))
//                    }
//                    val modelList :List<CheckboxModel> = newList
//                    option?.apply {
//                        setHasFixedSize(true)
//                        layoutManager = LinearLayoutManager(itemView.context)
//                        adapter = CheckBoxAdapter(modelList)
//                    }
                }
                else -> Log.e(QuestionListAdapter::class.java.simpleName, "No Question Type is Defined")
            }

        }

    }

}