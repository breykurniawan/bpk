package com.sis.app.activities

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.adapters.QuestionListAdapter
import com.sis.app.models.surveyData.QuestionReference
import com.sis.app.models.surveyData.SubSurvey
import com.sis.app.models.surveyData.Subs
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.surveyQuestion.Answer
import com.sis.app.models.surveyQuestion.RadioScaleModel
import com.sis.app.networks.Api
import kotlinx.android.synthetic.main.activity_detail_survey.*

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
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
            if (idx != 0) {
                rv_list_bagian.visibility = View.GONE
                progress.visibility = View.VISIBLE
                if (checkAnswer()) {
                    idxNow++
                    if (idxNow == idx) {
                        attemptFinish()
                    } else if (idxNow == (idx - 1)) {

                        next.text = "selesai"
                        populateNewData()

                    } else {
                        if (checkAnswer()) {
                            populateNewData()
                        }
                    }
                } else {
                    rv_list_bagian.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }
            } else {
                finish()
            }
        }
    }

    private fun attemptFinish() {
        viewAdapter.list!!.forEach {
            if (it.id_pertanyaan != -1) {
                answer.add(RadioScaleModel(it.id_pertanyaan, id_responden, 2, it.nilai))
            }
        }
        println("jumlah jawaban = ${answer.size}")
        parent_layout.foreground = ColorDrawable(resources.getColor(R.color.grey_transparent))
        progress.visibility = View.VISIBLE

        val call = Api().getInstance().sendAnswer(Answer(answer))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar.make(parent_layout, "Gagal Mengirim Jawaban", Snackbar.LENGTH_LONG).show()
                parent_layout.foreground = null
                progress.visibility = View.GONE
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val sn = Snackbar.make(parent_layout, "Gagal Mengirim Jawaban", Snackbar.LENGTH_LONG).show()
                    Handler().postDelayed({
                        finish()
                    }, 2000)
                } else {
                    parent_layout.foreground = null
                    progress.visibility = View.GONE
                }
            }

        })
    }

    private fun checkAnswer(): Boolean {
        viewAdapter!!.list!!.forEach {
            if (it.id_pertanyaan != -1 && it.nilai == -1) {
                Log.w(
                    DetailSurveyActivity::class.java.simpleName,
                    "jawaban tidak diisi: idPertanyaan ${it.id_pertanyaan}"
                )
                Snackbar.make(parent_layout, "Pertanyaan ${it.id_pertanyaan} Tidak Diisi", Snackbar.LENGTH_LONG).show()
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
                if (response.isSuccessful) {
                    progress.visibility = View.GONE
                    next.visibility = View.VISIBLE
                    body = response.body()
                    populateData()
                }
            }

        })

    }

    private fun populateData() {
        if (body?.bagian?.size == 0) {
            title_.text = "Tidak Ada Kuisioner Yang Harus Diisi"
            next.text = "selesai"
        } else {
            idx = body?.bagian?.size!!
            idxNow = 0
            println("banyak bagian $idx")
            title_.text = body?.bagian?.get(idxNow)?.judul
            var listPertanyaan = mutableListOf<QuestionReference>()
            val sub = body?.bagian?.get(idxNow)?.subs

            sub?.forEach { s ->
                listPertanyaan.add(QuestionReference(-1, -1, s.judul, "section", -1))
                s.pertanyaan.forEach { q ->
                    listPertanyaan.add(QuestionReference(q.id_pertanyaan, q.id_bagian, q.pertanyaan, "scale", -1))
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

    private fun populateNewData() {
        viewAdapter!!.list!!.forEach {
            println("jawaban $it")
        }
        viewAdapter!!.list!!.forEach {
            if (it.id_pertanyaan != -1) {
                answer.add(RadioScaleModel(it.id_pertanyaan, id_responden, 2, it.nilai))
            }
        }

        title_.text = body?.bagian?.get(idxNow)?.judul
        var listPertanyaan = mutableListOf<QuestionReference>()
        val sub = body?.bagian?.get(idxNow)?.subs

        sub?.forEach { s ->
            listPertanyaan.add(QuestionReference(-1, -1, s.judul, "section", -1))
            s.pertanyaan.forEach { q ->
                listPertanyaan.add(QuestionReference(q.id_pertanyaan, q.id_bagian, q.pertanyaan, "scale", -1))
            }
        }

        viewAdapter = QuestionListAdapter(listPertanyaan)


        rv_list_bagian.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        Handler().postDelayed({
            rv_list_bagian.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }, 1000)
    }

}
