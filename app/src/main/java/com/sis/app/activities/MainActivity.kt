package com.sis.app.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.fragments.Profile
import com.sis.app.fragments.SurveyList
import com.sis.app.fragments.SurveyRespondent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var surveyFragment: SurveyList? = null
    private var surveyRespondentFragment: SurveyRespondent? = null
    private var profileFragment: Profile? = null
    private var snackbar: Snackbar? = null
    private var tag: String = "survey"

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment = when (item.itemId) {
            R.id.nav_survey -> {
                supportActionBar?.title = "Daftar Survey"
                tag = "survey"
                surveyFragment ?: SurveyList()
            }
            R.id.nav_respondent -> {
                supportActionBar?.title = "Daftar Responden"
                tag = "responden"
                surveyRespondentFragment ?: SurveyRespondent()
            }
            R.id.nav_profile -> {
                supportActionBar?.title = "Profil"
                tag = "profil"
                profileFragment ?: Profile()
            }
            else -> {
                supportActionBar?.title = "Daftar Survey"
                tag = "survey"
                surveyFragment ?: SurveyList()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SurveyList(), tag).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag("survey") == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SurveyList(), "survey").commit()
            nav_view.selectedItemId = R.id.nav_survey
        } else {
            super.onBackPressed()
        }
    }

    public fun bar(msg:String) {
        Snackbar.make(parent_layout, msg, Snackbar.LENGTH_LONG).show()
    }
}
