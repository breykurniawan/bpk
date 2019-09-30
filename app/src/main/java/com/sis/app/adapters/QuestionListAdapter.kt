package com.sis.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sis.app.R
import com.sis.app.models.surveyData.Question
import com.sis.app.models.surveyData.QuestionReference
import com.sis.app.models.surveyQuestion.RadioScaleAnswer
import com.sis.app.models.surveyQuestion.RadioScaleModel
import com.sis.app.others.Utility

class QuestionListAdapter(val list: List<QuestionReference>?) :
    RecyclerView.Adapter<QuestionListAdapter.ViewHolder>() {

    //    companion object {
//    var scaleList: MutableList<RadioScaleAnswer> = mutableListOf()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListAdapter.ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.question_scale_five, parent, false),
//            viewType
//        )
//        if (list != null) {
//            println("banyaknya ${list.size}")
//            for (question in list) {
//                scaleList.add(RadioScaleAnswer(question.id_pertanyaan, question.tipe, -1))
//            }
//        }

        return when (viewType) {
            Utility.QUESTION_TYPE_TEXT -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_text, parent, false), viewType
            )
            Utility.QUESTION_TYPE_TEXTAREA -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_textarea, parent, false), viewType
            )
            Utility.QUESTION_TYPE_CHECKBOX -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_checkbox, parent, false), viewType
            )
            Utility.QUESTION_TYPE_RADIO -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_radio, parent, false), viewType
            )
            Utility.QUESTION_TYPE_SCALE -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_scale_five, parent, false), viewType
            )
            Utility.QUESTION_TYPE_SECTION -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_section, parent, false), viewType
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.question_empty, parent, false),
                viewType
            )
        }

    }

    override fun getItemViewType(position: Int): Int {
//        return Utility.QUESTION_TYPE_SCALE

        return when (list?.get(position)?.tipe) {
            "text " -> Utility.QUESTION_TYPE_TEXT
            "textarea" -> Utility.QUESTION_TYPE_TEXTAREA
            "checkbox" -> Utility.QUESTION_TYPE_CHECKBOX
            "radio" -> Utility.QUESTION_TYPE_RADIO
            "scale" -> Utility.QUESTION_TYPE_SCALE
            "section" -> Utility.QUESTION_TYPE_SECTION
            else -> 0
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

//    public fun getAnswer() = scaleList

    override fun onBindViewHolder(holder: QuestionListAdapter.ViewHolder, position: Int) {
        holder.bind(list?.get(position))

    }

    inner class ViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        private var title: TextView? = view.findViewById(R.id.title)
        private var scaleGroup: RadioGroup? = null
        var selectedScale = 0
//        private var option: ListView? = null

        init {
            when (viewType) {
                /*
                Utility.QUESTION_TYPE_TEXT -> null
                Utility.QUESTION_TYPE_TEXTAREA -> null
                Utility.QUESTION_TYPE_CHECKBOX -> option = view.findViewById(R.id.list_checkbox)
                Utility.QUESTION_TYPE_RADIO -> option = view.findViewById(R.id.list_radio)
                */
                Utility.QUESTION_TYPE_SCALE -> scaleGroup = view.findViewById(R.id.scale_group)
                Utility.QUESTION_TYPE_SECTION -> null
            }

            scaleGroup?.setOnCheckedChangeListener({ _, id ->
                when (id) {
                    R.id.scale_1 -> selectedScale = 1
                    R.id.scale_2 -> selectedScale = 2
                    R.id.scale_3 -> selectedScale = 3
                    R.id.scale_4 -> selectedScale = 4
                    R.id.scale_5 -> selectedScale = 5
                    else -> -1
                }
                list!!.get(adapterPosition).nilai = selectedScale
//                scaleList.get(adapterPosition).nilai = selectedScale
            })
        }

        fun bind(model: QuestionReference?) {
            title?.text = model?.judul

            /*
            when (model?.question_type) {
                "text " -> null
                "textarea" -> null
                "checkbox" -> {
                    val newList: ArrayList<CheckboxModel> = arrayListOf()
                    model.option_name?.forEach {
                        newList.add(CheckboxModel(it))
                    }
//                    val modelList :List<CheckboxModel> = newList
                    option?.apply {
                        adapter = CheckBoxAdapter(view.context, newList)
                    }
                }
                "radio" -> {
                    val newList: ArrayList<String> = arrayListOf()
                    model.option_name?.forEach {
                        newList.add(it)
                    }
                    option?.apply {
                        adapter = RadioButtonAdapter(view.context, newList)
                    }
//                    val newList: MutableList<CheckboxModel> = mutableListOf()
//                    model.option_name.forEach {
//                        newList.add(CheckboxModel(it))
//                    }
//                    val modelList :List<CheckboxModel> = newList
//                    option?.apply {
//                        setHasFixedSize(true)
//                        layoutManager = LinearLayoutManager(view.context)
//                        adapter = CheckBoxAdapter(modelList)
//                    }
                }
                "scale" -> {

                }
                else -> Log.e(QuestionListAdapter::class.java.simpleName, "No Question Type is Defined")
            }
            */
        }
    }
}
