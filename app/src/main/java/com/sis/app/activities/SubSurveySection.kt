package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sis.app.R
import com.sis.app.models.surveyData.SubSurvey
import com.sis.app.models.surveyData.Survey
import com.sis.app.networks.Api
import kotlinx.android.synthetic.main.activity_sub_survey_section.*
import retrofit2.Call
import retrofit2.Response

/**
 * Tidak Digunakan
 * Tampilan ini digunakan saat setelah mengisi data responden
 * dan sebelum memasuki tampilan kuisioner
 */
class SubSurveySection : AppCompatActivity() {

//    private lateinit var viewAdapter: SubSurveyAdapter
//    private var id_kuisioner: Int = -1
//    private var id_responden: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_survey_section)
//        id_kuisioner = intent.getIntExtra("id_kuisioner", -1)
//        id_responden = intent.getIntExtra("id_responden", -1)
//        getKuisioner(id_kuisioner)

//        var list: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")

    }

    fun getKuisioner(id_kuisioner: Int = -1) {
//        if (id_kuisioner == -1) {
//            Log.w(SubSurveySection::class.java.simpleName, "No id defined here")
//            return
//        }
//
//        val call: Call<Survey> = Api().getInstance().getQuestionnaire(id_kuisioner)
//        call.enqueue(object : retrofit2.Callback<Survey> {
//            override fun onFailure(call: Call<Survey>, t: Throwable) {
//                Handler().postDelayed({
//                    load_off.visibility = View.VISIBLE
//                    progress.visibility = View.GONE
//                }, 3000)
//            }
//
//            override fun onResponse(call: Call<Survey>, response: Response<Survey>) {
//                populateData(response.body())
//            }
//
//        })

    }

    fun populateData(data: Survey?) {
//        val bagian: List<SubSurvey>? = data?.bagian
//        viewAdapter = SubSurveyAdapter(bagian) {
//            val intent = Intent(applicationContext, DetailSurveyActivity::class.java)
////            intent.putExtra("model", it)
//            startActivity(intent)
//        }
//
//        rv_list_sub.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            adapter = viewAdapter
//        }
//        rv_list_sub.visibility = View.VISIBLE
//        progress.visibility = View.GONE
    }
}
