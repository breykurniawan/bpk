package com.survey.app.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.survey.app.R
import com.survey.app.fragments.Profile
import com.survey.app.fragments.SurveyList
import com.survey.app.fragments.SurveyResponse

class MainActivity : AppCompatActivity() {

    private var surveyFragment: SurveyList? = null
    private var surveyResponseFragment: SurveyResponse? = null
    private var profileFragment: Profile? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment = when (item.itemId) {
            R.id.nav_survey -> {
                surveyFragment?: SurveyList()
            }
            R.id.nav_response-> {
                surveyResponseFragment?: SurveyResponse()
            }
            R.id.nav_profile-> {
                profileFragment?: Profile()
            } else -> {
                surveyFragment?: SurveyList()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SurveyList()).commit()
    }
}
