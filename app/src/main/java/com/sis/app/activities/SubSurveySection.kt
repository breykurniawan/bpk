package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sis.app.R
import com.sis.app.adapters.SubSurveyAdapter
import com.sis.app.models.SubSurvey
import kotlinx.android.synthetic.main.activity_sub_survey_section.*

class SubSurveySection : AppCompatActivity() {

    private lateinit var viewAdapter: SubSurveyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_survey_section)

        var list: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")
        viewAdapter = SubSurveyAdapter(list) {
            val intent = Intent(applicationContext, DetailSurveyActivity::class.java)
            intent.putExtra("model", it)
            startActivity(intent)
        }
        rv_list_sub.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }
}
