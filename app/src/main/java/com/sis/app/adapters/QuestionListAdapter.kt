package com.sis.app.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.sis.app.R
import com.sis.app.models.surveyQuestion.QuestionReference
import com.sis.app.others.Utility

class QuestionListAdapter(val list: List<QuestionReference>?) :
    RecyclerView.Adapter<QuestionListAdapter.ViewHolder>() {

    //    companion object {
//    var scaleList: MutableList<RadioScaleAnswer> = mutableListOf()
//    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionListAdapter.ViewHolder {
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
                LayoutInflater.from(parent.context).inflate(R.layout.question_text, parent, false),
                viewType
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
                LayoutInflater.from(parent.context).inflate(R.layout.question_radio, parent, false),
                viewType
            )
            Utility.QUESTION_TYPE_SCALE -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_scale_five,
                    parent,
                    false
                ), viewType
            )
            Utility.QUESTION_TYPE_INSERT_PHOTOS -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_take_photos,
                    parent,
                    false
                ),
                viewType
            )
            Utility.QUESTION_TYPE_SECTION -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.question_section,
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
//        return Utility.QUESTION_TYPE_SCALE

        return when (list?.get(position)?.tipe) {
            Utility.QUESTION_TEXT -> Utility.QUESTION_TYPE_TEXT
            Utility.QUESTION_TEXTAREA -> Utility.QUESTION_TYPE_TEXTAREA
            Utility.QUESTION_CHECKBOX -> Utility.QUESTION_TYPE_CHECKBOX
            Utility.QUESTION_RADIO -> Utility.QUESTION_TYPE_RADIO
            Utility.QUESTION_SCALE -> Utility.QUESTION_TYPE_SCALE
            Utility.QUESTION_INSERT_PHOTOS -> Utility.QUESTION_TYPE_INSERT_PHOTOS
            Utility.QUESTION_SECTION -> Utility.QUESTION_TYPE_SECTION
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
        private var textAreaAnswer: TextInputEditText? = view.findViewById(R.id.answer_text_area)
        private var number: TextView = view.findViewById(R.id.number)
        private var scaleGroup: RadioGroup? = null
        var selectedScale = 0

        init {
            when (viewType) {
                Utility.QUESTION_TYPE_TEXTAREA -> {
                    textAreaAnswer?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            list!!.get(adapterPosition).nilai = p0.toString()
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })
                }
                Utility.QUESTION_TYPE_SCALE -> {
                    scaleGroup = view.findViewById(R.id.scale_group)
                    scaleGroup?.setOnCheckedChangeListener({ _, id ->
                        when (id) {
                            R.id.scale_1 -> selectedScale = 0
                            R.id.scale_2 -> selectedScale = 1
                            R.id.scale_3 -> selectedScale = 2
                            R.id.scale_4 -> selectedScale = 3
                            R.id.scale_5 -> selectedScale = 4
                            else -> -1
                        }
                        list!!.get(adapterPosition).nilai = "$selectedScale"
                    })
                }
                Utility.QUESTION_TYPE_INSERT_PHOTOS -> {

                }
                /*
                Utility.QUESTION_TYPE_TEXT -> null
                Utility.QUESTION_TYPE_CHECKBOX -> option = view.findViewById(R.id.list_checkbox)
                Utility.QUESTION_TYPE_RADIO -> option = view.findViewById(R.id.list_radio)
                */
                Utility.QUESTION_TYPE_SECTION -> null
            }
        }

        fun bind(model: QuestionReference?) {
            title?.text = model?.judul
            number.text = model?.nomor

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
