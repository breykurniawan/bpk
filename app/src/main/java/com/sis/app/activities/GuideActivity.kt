package com.sis.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sis.app.R
import com.sis.app.models.identity.RespondentData
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        setSupportActionBar(toolbar)

//        val idTipePemangku: Int = intent.getIntExtra("id_tipe_pemangku", -1)
        val data: RespondentData? = intent.getParcelableExtra("respondenData")
        val idKuisioner:Int = intent.getIntExtra("id_kuisioner", -1)
        val masyarakat = intent.getBooleanExtra("masyarakat", false)

        next.setOnClickListener {
            val intent: Intent = Intent(this@GuideActivity, DetailSurveyActivity::class.java)
            intent.putExtra("id_kuisioner", idKuisioner)
            intent.putExtra("respondenData", data)
            if (masyarakat) {
                intent.putExtra(("masyarakat"), true)
            }
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}