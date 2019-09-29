package com.sis.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sis.app.R
import com.sis.app.adapters.QuestionListAdapter
import com.sis.app.models.surveyData.QuestionReference
import com.sis.app.models.surveyData.SubSurvey
import com.sis.app.models.surveyData.Subs
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.surveyQuestion.RadioScaleModel
import com.sis.app.networks.Api
import kotlinx.android.synthetic.main.activity_detail_survey.*
import retrofit2.Call
import retrofit2.Response

class DetailSurveyActivity : AppCompatActivity() {

    private var kuisioner: List<SubSurvey>? = listOf()
    private lateinit var viewAdapter: QuestionListAdapter
    private var id_kuisioner: Int = -1
    private var id_responden: Int = -1

    private var idx: Int = -1
    private var idxNow = -1
    var answer: MutableList<RadioScaleModel> = mutableListOf()
    var body: Survey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_survey)
        id_kuisioner = intent.getIntExtra("id_kuisioner", -1)
        id_responden = intent.getIntExtra("id_responden", -1)
        getKuisioner(id_kuisioner)

        next.setOnClickListener {
            idxNow++
            if (idxNow > idx) {
                attemptFinish()
            } else if (idxNow == idx.minus(1)) {
//                if(checkAnswer()) {
                    next.text = "selesai"
//                    populateNewData()
//                }
            } else {
                /*if(checkAnswer()) */populateNewData()
            }
        }
    }

    private fun attemptFinish() {
        println(answer.toString())
        finish()
    }

    private fun checkAnswer(): Boolean {
        QuestionListAdapter.scaleList.forEach {
            if(it.nilai == -1) {
                Log.w(DetailSurveyActivity::class.java.simpleName, "jawaban tidak diisi: idPertanyaan ${it.id_pertanyaan}")
                return false
            }
        }
        return true
    }

    fun getKuisioner(id_kuisioner: Int = -1) {
        if (id_kuisioner == -1) {
            Log.w(SubSurveySection::class.java.simpleName, "No id defined here")
            return
        }

        val call: Call<Survey> = Api().getInstance().getQuestioner(id_kuisioner)
        call.enqueue(object : retrofit2.Callback<Survey> {
            override fun onFailure(call: Call<Survey>, t: Throwable) {
                Handler().postDelayed({
                    progress.visibility = View.GONE
                    load_off.visibility = View.VISIBLE
                }, 3000)
            }

            override fun onResponse(call: Call<Survey>, response: Response<Survey>) {
                progress.visibility = View.GONE
                body = response.body()
                populateData()
            }

        })

    }

    private fun populateData() {
        idx = body?.bagian?.size!!
        idxNow = 0
        title_.text = body?.bagian?.get(idxNow)?.judul
        var listPertanyaan = mutableListOf<QuestionReference>()
        val sub = body?.bagian?.get(idxNow)?.subs

        sub?.forEach { s ->
            listPertanyaan.add(QuestionReference(-1, -1, s.judul, "section"))
            s.pertanyaan.forEach { q ->
                listPertanyaan.add(QuestionReference(q.id_pertanyaan, q.id_bagian, q.pertanyaan, "scale"))
            }
        }

        viewAdapter = QuestionListAdapter(listPertanyaan)


        rv_list_bagian.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun populateNewData() {
        QuestionListAdapter.scaleList.forEach {
            answer.add(RadioScaleModel(it.id_pertanyaan, id_responden, 2, it.nilai))
        }
        QuestionListAdapter.scaleList = mutableListOf()
        title_.text = body?.bagian?.get(idxNow)?.judul
        var listPertanyaan = mutableListOf<QuestionReference>()
        val sub = body?.bagian?.get(idxNow)?.subs

        sub?.forEach { s ->
            listPertanyaan.add(QuestionReference(-1, -1, s.judul, "section"))
            s.pertanyaan.forEach { q ->
                listPertanyaan.add(QuestionReference(q.id_pertanyaan, q.id_bagian, q.pertanyaan, "scale"))
            }
        }

        viewAdapter = QuestionListAdapter(listPertanyaan)


        rv_list_bagian.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

}
