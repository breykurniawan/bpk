package com.sis.app.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.sis.app.R
import com.sis.app.activities.InputIdentityActivity
import com.sis.app.adapters.SurveyAdapter
import com.sis.app.models.surveyData.Data
import com.sis.app.models.surveyData.ListSurvey
import kotlinx.android.synthetic.main.fragment_survey_list.*

class SurveyList : Fragment() {

    private lateinit var viewAdapter: SurveyAdapter
    private lateinit var list: List<ListSurvey>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey_list, container, false)
        val json = """
            {"data":[{"id_kuisioner":1,"judul":"KUESIONER SURVEI PENGUKURAN INDEKS INDIKATOR KINERJA DAN PMPRB BPK 2019","deskripsi":"Kuisoner ini untuk mengukur indeks indikator kinerja dan PMPRB BPK pada tahun 2019","tipe_responden":1,"created_at":"2019-09-26 20:48:21","updated_at":"-0001-11-30 00:00:00"}]}
        """.trimIndent()

        val data = Gson().fromJson(json, Data::class.java)
        list = data.data

        viewAdapter = SurveyAdapter(list) { d ->
            val intent = Intent(activity, InputIdentityActivity::class.java)
            intent.putExtra("id_kuisioner", d)
            startActivity(intent)
        }


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity!!.title = "Daftar Survey"
        rv_list_survey.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }

        Handler().postDelayed({
            progress.visibility = View.GONE
            rv_list_survey.visibility = View.VISIBLE
        }, 3000)
    }




}
