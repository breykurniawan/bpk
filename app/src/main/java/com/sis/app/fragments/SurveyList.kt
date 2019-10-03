package com.sis.app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
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
import com.google.gson.Gson

import com.sis.app.R
import com.sis.app.activities.InputIdentityActivity
import com.sis.app.adapters.SurveyAdapter
import com.sis.app.models.surveyData.Data
import com.sis.app.models.surveyData.ListSurvey
import com.sis.app.networks.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SurveyList : Fragment() {

    private var viewAdapter: SurveyAdapter? = null
    private var listSurvey: List<ListSurvey>? = listOf()
    private lateinit var rv: RecyclerView
    private lateinit var progress: SpinKitView
    private lateinit var ptr: SwipeRefreshLayout
    private lateinit var bg: ImageView
    private lateinit var text_bg: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_survey_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rv_list_survey)
        progress = view.findViewById(R.id.progress)
        ptr = view.findViewById(R.id.ptr)
        bg = view.findViewById(R.id.bg)
        text_bg = view.findViewById(R.id.text_bg)


        ptr.setOnRefreshListener {
            rv.visibility = View.GONE
            progress.visibility = View.VISIBLE
            Handler().postDelayed({
                ptr.isRefreshing = false
            }, 500)
            Handler().postDelayed({
                populateData()
            }, 2000)
        }
        populateData()
    }

    fun populateData() {
        val call: Call<List<ListSurvey>> = Api().getInstance().getListKuisioner()
        call.enqueue(object : Callback<List<ListSurvey>> {
            override fun onFailure(call: Call<List<ListSurvey>>, t: Throwable) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Gagal Menerima Data",
                    Toast.LENGTH_LONG
                ).show()
                bg.visibility = View.VISIBLE
                text_bg.visibility = View.VISIBLE
                Log.e(SurveyList::class.java.simpleName, "Cannot Load Data: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<ListSurvey>>,
                response: Response<List<ListSurvey>>
            ) {
                if (response.isSuccessful) {
                    listSurvey = response.body()
                    viewAdapter = SurveyAdapter(listSurvey) { d ->
                        val intent = Intent(activity, InputIdentityActivity::class.java)
                        intent.putExtra("id_kuisioner", d)
                        startActivity(intent)
                    }
                    rv.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = viewAdapter
                    }
                    progress.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Gagal Menerima Data",
                        Toast.LENGTH_LONG
                    ).show()
                    bg.visibility = View.VISIBLE
                    text_bg.visibility = View.VISIBLE
                    Log.w(
                        SurveyList::class.java.simpleName,
                        "Gagal Menerima Data: ${response.message()}"
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
