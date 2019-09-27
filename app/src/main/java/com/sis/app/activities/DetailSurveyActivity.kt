package com.sis.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sis.app.R
import com.sis.app.adapters.QuestionListAdapter
import com.sis.app.models.SubSurvey
import kotlinx.android.synthetic.main.activity_detail_survey.*

class DetailSurveyActivity : AppCompatActivity() {

    private lateinit var viewAdapter: QuestionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_survey)

        val data: SubSurvey? = intent.getParcelableExtra("model")
        viewAdapter = QuestionListAdapter(data?.question)

        rv_list_question.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

    }
}
