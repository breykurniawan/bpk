package com.sis.app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.ybq.android.spinkit.SpinKitView

import com.sis.app.R
import com.sis.app.activities.InputIdentityActivity
import com.sis.app.activities.MainActivity
import com.sis.app.adapters.RespondentDataAdapter
import com.sis.app.adapters.SurveyAdapter
import com.sis.app.models.surveyData.ListSurvey
import com.sis.app.models.surveyData.RespondentSurveyData
import com.sis.app.networks.Api
import com.sis.app.others.TinyDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyRespondent : Fragment() {

    private var id_user: Int = -1
    private var viewAdapter: RespondentDataAdapter? = null
    private lateinit var rv: RecyclerView
    private lateinit var ptr: SwipeRefreshLayout
    private lateinit var bg: ImageView
    private lateinit var progress: SpinKitView
    private lateinit var text_respondent: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_respondent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rv_list_survey_respondent)
        progress = view.findViewById(R.id.progress)
        ptr = view.findViewById(R.id.ptr)
        bg = view.findViewById(R.id.bg)
        text_respondent = view.findViewById(R.id.text_respondent)

        id_user = TinyDB(activity?.applicationContext).getInt("idSurveyor")
        ptr.setOnRefreshListener {
            progress.visibility = View.VISIBLE
            rv.visibility = View.GONE
            Handler().postDelayed({
                ptr.isRefreshing = false
            }, 500)
            Handler().postDelayed({
                populateData()
            }, 2000)
        }
        populateData()
    }

    private fun populateData() {
        val call: Call<List<RespondentSurveyData>> = Api().getInstance().getListRespondent(id_user)
        call.enqueue(object : Callback<List<RespondentSurveyData>> {
            override fun onFailure(call: Call<List<RespondentSurveyData>>, t: Throwable) {
                Toast.makeText(activity?.applicationContext, "Gagal Menerima Data", Toast.LENGTH_LONG).show()
                bg.visibility = View.VISIBLE
                text_respondent.visibility = View.VISIBLE
                progress.visibility = View.GONE
                Log.e(SurveyList::class.java.simpleName, "Cannot Load Data: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<RespondentSurveyData>>,
                response: Response<List<RespondentSurveyData>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.size != 0) {
                        viewAdapter = RespondentDataAdapter(response.body())
                        rv.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(activity)
                            adapter = viewAdapter
                        }
                        progress.visibility = View.GONE
                        bg.visibility = View.GONE
                        text_respondent.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                    } else {
                        progress.visibility = View.GONE
                        bg.visibility = View.VISIBLE
                        text_respondent.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(activity?.applicationContext, "Gagal Menerima Data", Toast.LENGTH_LONG).show()
                    bg.visibility = View.VISIBLE
                    text_respondent.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                    Log.e(
                        SurveyList::class.java.simpleName,
                        "Cannot Load Data: ${response.message()}"
                    )
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }


}
