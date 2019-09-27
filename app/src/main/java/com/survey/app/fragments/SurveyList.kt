package com.survey.app.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.survey.app.R
import com.survey.app.activities.DetailSurveyActivity
import com.survey.app.adapters.QuestionListAdapter
import com.survey.app.adapters.SurveyAdapter
import com.survey.app.models.Survey
import com.survey.app.models.SurveyDesc
import kotlinx.android.synthetic.main.fragment_survey_list.*

class SurveyList : Fragment() {

    private lateinit var viewAdapter: SurveyAdapter
    private var list: MutableList<SurveyDesc> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey_list, container, false)
        viewAdapter = SurveyAdapter(list) {
            val intent = Intent(activity, DetailSurveyActivity::class.java)
            intent.putExtra("model", 2)
        }


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list_survey.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
//            adapter
        }
    }


}
