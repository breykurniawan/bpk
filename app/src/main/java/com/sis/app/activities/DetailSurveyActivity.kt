package com.sis.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sis.app.R
import com.sis.app.models.SurveyDesc

class DetailSurveyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)
        intent.getParcelableExtra<SurveyDesc>("model")
    }
}
