package com.sis.app.activities

import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.adapters.QuestionListAdapter
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.identity.RespondentData
import com.sis.app.models.surveyData.QuestionReference
import com.sis.app.models.surveyData.SubSurvey
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.surveyQuestion.Answer
import com.sis.app.models.surveyQuestion.AnswerModel
import com.sis.app.networks.Api
import com.sis.app.others.TinyDB
import com.sis.app.others.Utility
import kotlinx.android.synthetic.main.activity_detail_survey.*

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSurveyActivity : AppCompatActivity() {

    private var viewAdapter: QuestionListAdapter? = null
    private var answer: MutableList<AnswerModel> = mutableListOf()
    private var body: Survey? = null
    private var idx: Int = -1
    private var idxNow = -1
    private var id_kuisioner: Int = -1
    private var id_responden: Int = -1
    private var id_user: Int = -1
    private var kuisioner: List<SubSurvey>? = listOf()
    private var networkStatus: Boolean = true
    private var snackbar: Snackbar? = null
    private var data: RespondentData? = null
    private var finishing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_survey)
        setSupportActionBar(toolbar)


        data = intent.getParcelableExtra("respondenData")
        id_kuisioner = intent.getIntExtra("id_kuisioner", -1)
        id_user = TinyDB(applicationContext).getInt("idSurveyor")
        getKuisioner(id_kuisioner)

        next.setOnClickListener {
            if (finishing) attemptFinish()
            else nextPart()
        }
    }

    override fun onBackPressed() {
        warnDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun nextPart() {
        if (idx != 0) {
            rv_list_bagian.visibility = View.GONE
            progress.visibility = View.VISIBLE
            if (checkAnswer()) {
                idxNow++
                if (idxNow == idx) {
                    attemptFinish()
                } else if (idxNow == (idx - 1)) {
                    next.text = "selesai"
                    populateData()
                } else {
                    populateData()
                }
            } else {
                rv_list_bagian.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }
        } else {
            finish()
        }
    }

    private fun attemptFinish() {
        if (!finishing) {
            finishing = true
            insertAnswer()
        }
        title_.text = "Mengirim..."
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
            ColorDrawable(resources.getColor(R.color.grey_transparent))
        else overlay.visibility = View.VISIBLE
        progress.visibility = View.VISIBLE
        sendRespondentData()
    }

    private fun sendQuestionnaire() {
        var answerSend: MutableList<AnswerModel> = mutableListOf()
        answer.forEach {
            answerSend.add(AnswerModel(it.id_pertanyaan, id_responden, it.id_user, it.nilai))
        }
        val call = Api().getInstance().sendAnswer(Answer(answerSend))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar.make(parent_layout, "Gagal Mengirim Data", Snackbar.LENGTH_LONG).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground = null
                else overlay.visibility = View.GONE
                progress.visibility = View.GONE
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val sn = Snackbar.make(parent_layout, "Data Tersimpan", Snackbar.LENGTH_LONG).show()
                    Handler().postDelayed({
                        finish()
                    }, 2000)
                } else {
                    val sn = Snackbar.make(
                        parent_layout,
                        "Gagal Mengirim Data : ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                        null
                    else overlay.visibility = View.GONE
                    progress.visibility = View.GONE
                }
            }
        })
    }

    private fun sendRespondentData() {

        val call = Api().getInstance().sendDataRespondent(
            data!!.nama,
            data!!.alamat,
            data!!.jenis_kelamin,
            data!!.no_hp,
            data!!.nama_instansi,
            data!!.tipe_stakeholder!!,
            data!!.nama_stakeholder!!,
            data!!.domisili!!,
            data!!.usia
        )
        call.enqueue(object : Callback<DataResponse> {
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Snackbar.make(
                    parent_layout,
                    "Gagal Mengirim Data Responden",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                logging("Cannot send data: ${t.message}")
            }

            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    id_responden = response.body()!!.id_responden
                    sendQuestionnaire()
                } else {
                    Snackbar.make(
                        parent_layout,
                        "Gagal Menerima ID Responden",
                        Snackbar.LENGTH_LONG
                    ).show()
                    logging("problem ${response.errorBody().toString()}")
                    logging("problem ${response}")
                    logging("IDresponden = ${response.body()?.id_responden}")
                }
            }
        })
    }

    private fun checkAnswer(): Boolean {
        viewAdapter
            ?.list!!.forEach {
            if (it.id_pertanyaan != -1 && it.nilai == -1) {
                Log.w(
                    DetailSurveyActivity::class.java.simpleName,
                    "jawaban tidak diisi: idPertanyaan ${it.id_pertanyaan}"
                )
                Snackbar.make(
                    parent_layout,
                    "Pertanyaan ${it.id_pertanyaan} Tidak Diisi",
                    Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }

    private fun getKuisioner(id_kuisioner: Int = -1) {
        if (id_kuisioner == -1) {
            Log.w(SubSurveySection::class.java.simpleName, "No id defined here")
            return
        }

        val call: Call<Survey> = Api().getInstance().getQuestionnaire(id_kuisioner)
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
            if (idx == -1) {
                idx = body?.bagian?.size!!
                idxNow = 0
            } else {
                insertAnswer()
            }
            title_.text = body?.bagian?.get(idxNow)?.judul
            var listPertanyaan = mutableListOf<QuestionReference>()
            val sub = body?.bagian?.get(idxNow)?.subs

            sub?.forEach { s ->
                listPertanyaan.add(QuestionReference(-1, -1, s.judul, Utility.QUESTION_SECTION, -1))
                s.pertanyaan.forEach { q ->
                    listPertanyaan.add(
                        QuestionReference(
                            q.id_pertanyaan, q.id_bagian, q.pertanyaan, Utility.QUESTION_SCALE, -1
                        )
                    )
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

    /*
    private fun populateNewData() {
//        viewAdapter!!.list!!.forEach {
//            println("jawaban $it")
//        }


        title_.text = body?.bagian?.get(idxNow)?.judul
        var listPertanyaan = mutableListOf<QuestionReference>()
        val sub = body?.bagian?.get(idxNow)?.subs

        sub?.forEach { s ->
            listPertanyaan.add(QuestionReference(-1, -1, s.judul, "section", -1))
            s.pertanyaan.forEach { q ->
                listPertanyaan.add(
                    QuestionReference(
                        q.id_pertanyaan,
                        q.id_bagian,
                        q.pertanyaan,
                        "scale",
                        -1
                    )
                )
            }
        }

        viewAdapter = QuestionListAdapter(listPertanyaan)


        rv_list_bagian.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }
    */

    private fun warnDialog() {
        val dialog: AlertDialog.Builder? = AlertDialog.Builder(this@DetailSurveyActivity)
        dialog?.setMessage("Batal Mengisi Kuisioner?")
            ?.setPositiveButton("Ya", { dialog, id ->
                finish()
            })
            ?.setNegativeButton("Tidak", { dialog, id ->
                dialog.dismiss()
            })
        dialog?.create()?.show()
    }

    private fun logging(msg: String) = Log.w(InputIdentityActivity::class.java.simpleName, msg)

    private fun insertAnswer() {
        viewAdapter?.list!!.forEach {
            if (it.id_pertanyaan != -1) {
                answer.add(AnswerModel(it.id_pertanyaan, id_responden, id_user, it.nilai))
            }
        }
    }

}
