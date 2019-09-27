package com.sis.app.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.sis.app.R
import com.sis.app.activities.DetailSurveyActivity
import com.sis.app.activities.InputIdentityActivity
import com.sis.app.activities.SubSurveySection
import com.sis.app.adapters.SurveyAdapter
import com.sis.app.models.ListSurvey
import com.sis.app.models.Survey
import kotlinx.android.synthetic.main.fragment_survey_list.*

class SurveyList : Fragment() {

    private lateinit var viewAdapter: SurveyAdapter
    private lateinit var list: List<Survey>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey_list, container, false)
        val json = """
            {"survey":[{"id":1,"title":"Kuesioner BPK","description":"KUESIONER SURVEI PENGUKURAN INDEKS INDIKATOR KINERJA DAN PMPRB BPK 2019","subssurvey":[{"id":1,"title":"TINGKAT RELEVANSI PEMERIKSAAN DENGAN HARAPAN DAN KEBUTUHAN PEMANGKU KEPENTINGAN","description":"...","question":[{"id":1,"title":"PEMANFAATAN LAPORAN HASIL PEMERIKSAAN (LHP)","question_type":"section","option_name":null},{"id":2,"title":"Lingkup pemeriksaan BPK sesuai dengan harapan","question_type":"scale","option_name":null},{"id":3,"title":"LHP bermanfaat untuk meningkatkan pengelolaan dan pertanggungjawaban keuangan negara","question_type":"scale","option_name":null},{"id":4,"title":"Materi yang disajikan dalam LHP cukup jelas","question_type":"scale","option_name":null},{"id":5,"title":"LHP sesuai dengan kondisi sebenarnya","question_type":"scale","option_name":null},{"id":6,"title":"Penyampaian LHP atas Laporan Keuangan tepat waktu.\n(Tidak lebih dari 2 bulan sejak Laporan Keuangan disampaikan ke BPK)","question_type":"scale","option_name":null},{"id":7,"title":"Data pendukung atas temuan yang dimuat dalam LHP cukup lengkap dan valid","question_type":"scale","option_name":null},{"id":8,"title":"Rekomendasi dalam LHP sesuai dengan temuan pemeriksaan ","question_type":"scale","option_name":null},{"id":9,"title":"Rekomendasi dalam LHP cukup jelas untuk ditindaklanjuti","question_type":"scale","option_name":null},{"id":10,"title":"Komunikasi antara BPK dengan pemangku kepentingan cukup efektif dalam menindak lanjuti rekomendasi LHP","question_type":"scale","option_name":null},{"id":11,"title":"SARAN RESPONDEN UNTUK PEMANFAATAN LHP","question_type":"section","option_name":null},{"id":12,"title":"...","question_type":"textarea","option_name":null}]},{"id":2,"title":"INDEKS KEPUASAN AUDITEE ATAS KINERJA PEMERIKSA BPK","description":"...","question":[{"id":1,"title":"INTEGRITAS","question_type":"section","option_name":null},{"id":2,"title":"Pemeriksa BPK bersikap jujur dalam menerapkan prinsip, nilai dan keputusan pemeriksaan ","question_type":"scale","option_name":null},{"id":3,"title":"Pemeriksa BPK objektif dalam menerapkan prinsip, nilai dan keputusan pemeriksaan","question_type":"scale","option_name":null},{"id":4,"title":"Pemeriksa BPK tegas dalam menerapkan prinsip, nilai dan keputusan pemeriksaan","question_type":"scale","option_name":null},{"id":5,"title":"Pemeriksa BPK taat pada peraturan yang berlaku","question_type":"scale","option_name":null},{"id":6,"title":"INDEPENDENSI","question_type":"section","option_name":null},{"id":7,"title":"Pemeriksa BPK bersikap jujur dalam menerapkan prinsip, nilai dan keputusan pemeriksaan ","question_type":"scale","option_name":null},{"id":8,"title":"Pemeriksa BPK objektif dalam menerapkan prinsip, nilai dan keputusan pemeriksaan","question_type":"scale","option_name":null},{"id":9,"title":"PROFESIONALISME","question_type":"section","option_name":null},{"id":10,"title":"Pemeriksa BPK berhati-hati dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":11,"title":"Pemeriksa BPK teliti dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":12,"title":"Pemeriksa BPK cermat dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":13,"title":"Pemeriksa BPK berpedoman pada standar yang berlaku dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":14,"title":"Pemeriksa BPK berkomunikasi secara efektif dan sesuai dengan norma yang berlaku, baik lisan maupun tulisan dengan auditee","question_type":"scale","option_name":null},{"id":15,"title":"SARAN RESPONDEN UNTUK KINERJA PEMERIKSA BPK","question_type":"section","option_name":null},{"id":16,"title":"...","question_type":"textarea","option_name":null}]},{"id":2,"title":"INDEKS KEPUASAN AUDITEE ATAS KINERJA PEMERIKSA BPK","description":"...","question":[{"id":1,"title":"DISEMINASI INFORMASI (INFORMATION CONTENT)","question_type":"section","option_name":null},{"id":2,"title":"BPK menyebarluaskan informasi yang akurat tentang tugas dan wewenang BPK serta hasil pemeriksaan di berbagai media","question_type":"scale","option_name":null},{"id":3,"title":"BPK menyebarluaskan informasi terkini (up to date) kepada publik","question_type":"scale","option_name":null},{"id":4,"title":"BPK menyebarluaskan informasi yang lengkap dari sisi konten informasi kepada publik","question_type":"scale","option_name":null},{"id":5,"title":"BPK menyebarluaskan informasi yang sesuai dengan kebutuhan pengguna informasi","question_type":"scale","option_name":null},{"id":6,"title":"BPK telah menyebarluaskan informasi secara berkala setiap bulan","question_type":"scale","option_name":null},{"id":7,"title":"MEDIA KOMUNIKASI (MEDIA CHANNEL)","question_type":"section","option_name":null},{"id":8,"title":"BPK menyediakan media informasi dan komunikasi yang mudah diakses oleh pemangku kepentingan","question_type":"scale","option_name":null},{"id":9,"title":"BPK menyebarluaskan informasi melalui media sosial yang cukup lengkap, seperti twitter, instagram, facebook, youtube","question_type":"scale","option_name":null},{"id":10,"title":"Pemeriksa BPK berhati-hati dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":11,"title":"Pemeriksa BPK teliti dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":12,"title":"Pemeriksa BPK cermat dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":13,"title":"Pemeriksa BPK berpedoman pada standar yang berlaku dalam proses pemeriksaan","question_type":"scale","option_name":null},{"id":14,"title":"Pemeriksa BPK berkomunikasi secara efektif dan sesuai dengan norma yang berlaku, baik lisan maupun tulisan dengan auditee","question_type":"scale","option_name":null},{"id":15,"title":"SARAN RESPONDEN UNTUK KINERJA PEMERIKSA BPK","question_type":"section","option_name":null},{"id":16,"title":"...","question_type":"textarea","option_name":null}]}]}]}
        """.trimIndent()

        val data = Gson().fromJson(json, ListSurvey::class.java)
        list = data.survey

        viewAdapter = SurveyAdapter(list) {
            val intent = Intent(activity, InputIdentityActivity::class.java)
            intent.putParcelableArrayListExtra("model", it as java.util.ArrayList<out Parcelable>)
            startActivity(intent)
        }


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list_survey.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }
    }


}
