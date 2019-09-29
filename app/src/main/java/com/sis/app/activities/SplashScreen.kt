package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sis.app.R

class SplashScreen : AppCompatActivity() {

    /*
    TODO this
    SurveyList > ListSurvey
    InputID > id_kuisioner
    SubSurveySection > Load Survey > bagian
    DetailSurvey > subs > pertanyaan

    loading
    login
    tambah tipe stakeholder
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
        }, 3000)
    }
}
