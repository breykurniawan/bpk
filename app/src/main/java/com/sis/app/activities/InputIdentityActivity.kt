package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.models.identity.RespondentData
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.identity.Stakeholder
import com.sis.app.models.identity.StakeholderType
import com.sis.app.networks.Api
import kotlinx.android.synthetic.main.activity_input_identity.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputIdentityActivity : AppCompatActivity() {

    private var stakeholderTypeSelected: Int? = -1
    private var stakeholdersSelected: Int? = -1
    private var residenceSelected: Int = -1
    var genderSelected: Int = -1
    private var listStakeholderType: List<StakeholderType>? = listOf()
    private var listStakeholder: List<Stakeholder>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_identity)
//        val model: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")
        val id_kuisioner: Int = intent.getIntExtra("id_kuisioner", -1)
        getSpinnerData()

        val dmsl =
            ArrayAdapter.createFromResource(this, R.array.domisili, R.layout.spinner_item)
        dmsl.setDropDownViewResource(R.layout.spinner_item)
        residence.adapter = dmsl

        gender.setOnCheckedChangeListener { _, i ->
            if (i == R.id.gender_man) {
                genderSelected = 1
                println("laki")
            } else if (i == R.id.gender_woman) {
                println("perempuan")
                genderSelected = 2
            }

//            check = (i == R.id.gender_man) || (i == R.id.gender_woman)
        }

        next.setOnClickListener {
            invalidateInputAndSend(id_kuisioner);
//            val intent = Intent(applicationContext, DetailSurveyActivity::class.java)
//            intent.putExtra("id_kuisioner", id_kuisioner)
//            intent.putExtra("id_responden", 24)
//            startActivity(intent)
        }
    }

    fun getSpinnerData() {
        val call: Call<List<StakeholderType>> = Api().getInstance().getStakeholderType()
        call.enqueue(object : Callback<List<StakeholderType>> {
            override fun onFailure(call: Call<List<StakeholderType>>, t: Throwable) {
                Snackbar.make(parent_layout, "Gagal Menerima Data", Snackbar.LENGTH_LONG).show()
                next.isEnabled = false
                logging("Cannot get Stakeholder Type: ${t.message}")
            }

            override fun onResponse(call: Call<List<StakeholderType>>, response: Response<List<StakeholderType>>) {
                if (response.isSuccessful) {
                    listStakeholderType = response.body()
                    populateSpinner()
                } else {
                    Snackbar.make(parent_layout, "Gagal Menerima Data\n${response.message()}", Snackbar.LENGTH_LONG).show()
                    logging("Gagal Menerima Data\n${response.message()}")
                }
            }

        })
    }

    private fun invalidateInputAndSend(id_kuisioner: Int) {
        var check: Boolean = true

        check != TextUtils.isEmpty(name.text.toString())
        if (TextUtils.isEmpty(name.text.toString())) name.error = "Harus Diisi"

        check != TextUtils.isEmpty(address.text.toString())
        if (TextUtils.isEmpty(address.text.toString())) address.error = "Harus Diisi"

        check != TextUtils.isEmpty(phone.text.toString())
        if (TextUtils.isEmpty(phone.text.toString())) phone.error = "Harus Diisi"

        check != TextUtils.isEmpty(agency.text.toString())
        if (TextUtils.isEmpty(agency.text.toString())) agency.error = "Harus Diisi"

//        check = stakeholdersSelected != -1 || stakeholdersSelected != 0

        println(check)
        logging("jk $genderSelected")
        if (check) {
            val rs = residenceSelected + 1
            val call = Api().getInstance().sendDataRespondent(
                name.text.toString(),
                address.text.toString(),
                genderSelected,
                phone.text.toString(),
                agency.text.toString(),
                stakeholderTypeSelected!!,
                stakeholdersSelected!!,
                rs
            )
            call.enqueue(object : Callback<DataResponse> {
                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    Snackbar.make(parent_layout, "Gagal Mengirim", Snackbar.LENGTH_LONG).show()
                    logging("Cannot send data: ${t.message}")
                }

                override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                    println("ID Responden ${response.body()?.id_responden}")
                    if (response.isSuccessful) {
                        val intent = Intent(applicationContext, DetailSurveyActivity::class.java)
                        intent.putExtra("id_kuisioner", id_kuisioner)
                        intent.putExtra("id_responden", response.body()?.id_responden)
                        startActivity(intent)
                    } else {
                        Snackbar.make(parent_layout, "Gagal Menerima", Snackbar.LENGTH_LONG).show()
                        logging("problem ${response.errorBody().toString()}")
                        logging("problem ${response}")
                        logging("IDresponden = ${response.body()?.id_responden}")
                    }
                }

            })
        }
    }

    private fun populateSpinner() {
        val lst: MutableList<String> = mutableListOf()
        listStakeholderType?.forEach {
            lst.add(it.nama_tipe)
        }

        ArrayAdapter(applicationContext, R.layout.spinner_item, lst)
            .also {
                it.setDropDownViewResource(R.layout.spinner_item)
                stakeholders_type.adapter = it
            }

        ArrayAdapter(applicationContext, R.layout.spinner_item, listOf("..."))
            .also {
                it.setDropDownViewResource(R.layout.spinner_item)
                stakeholders.adapter = it
            }

        residence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                println("posisi $pos dan id $id")
                residenceSelected = pos
            }
        }

        stakeholders_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                println("posisi $pos dan id $id")
                stakeholderTypeSelected = listStakeholderType?.get(pos)?.id
                listStakeholderType?.get(pos)?.id
                listStakeholder = listStakeholderType?.get(pos)?.pemangku_kepentingan
                val st: MutableList<String> = mutableListOf()
                listStakeholder?.forEach {
                    st.add(it.nama)
                }
                ArrayAdapter(applicationContext, R.layout.spinner_item, st)
                    .also {
                        it.setDropDownViewResource(R.layout.spinner_item)
                        stakeholders.adapter = it
                    }

            }

        }

        stakeholders.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                stakeholdersSelected = listStakeholder?.get(pos)?.id_pemangku_kepentingan
                println(listStakeholder?.get(pos)?.id_pemangku_kepentingan)
            }

        }
    }

    fun logging(msg: String) = Log.w(InputIdentityActivity::class.java.simpleName, msg)

//     fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//        println("posisi ${parent.selectedItemId.toInt()} dan posisi $pos")
//        when {
//            parent.id == R.id.stakeholders_type -> {
//                stakeholderTypeSelected = listStakeholderType?.get(parent.selectedItemId.toInt())?.id
//                listStakeholder = listStakeholderType?.get(pos)?.pemangku_kepentingan
//                val lst: MutableList<String> = mutableListOf()
//                listStakeholder?.forEach {
//                    lst.add(it.nama)
//                }
//
//                ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, lst)
//                    .also {
//                        it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//                        stakeholders.adapter = it
//                    }
//
//            }
//            parent.id == R.id.stakeholders -> stakeholdersSelected = listStakeholder?.get(pos)?.id_pemangku_kepentingan
//            parent.id == R.id.residence -> residenceSelected = pos
//        }
//    }
//
//    fun onNothingSelected(parent: AdapterView<*>) {
//        // Another interface callback
//    }

}
