package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.sis.app.R
import com.sis.app.models.identity.RespondentData
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.identity.Stakeholder
import com.sis.app.models.identity.StakeholderType
import com.sis.app.networks.Api
import kotlinx.android.synthetic.main.activity_input_identity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputIdentityActivity : AppCompatActivity() {

    private var stakeholderTypeSelected: Int? = -1
    private var stakeholdersSelected: Int? = -1
    private var residenceSelected: Int = -1
    private var listStakeholderType: List<StakeholderType>? = listOf()
    private var listStakeholder: List<Stakeholder>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_identity)
//        val model: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")
        val id_kuisioner: Int = intent.getIntExtra("id_kuisioner", -1)
        getSpinnerData()

        val dmsl =
            ArrayAdapter.createFromResource(this, R.array.domisili, R.layout.support_simple_spinner_dropdown_item)
        dmsl.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        residence.adapter = dmsl

        next.setOnClickListener {
//            var id_responden: Int? = invalidateInputAndSend();
//            if (id_responden != -1) {
                val intent = Intent(this, DetailSurveyActivity::class.java)
//            intent.putParcelableArrayListExtra("model", model as ArrayList<out Parcelable>)
                intent.putExtra("id_kuisioner", id_kuisioner)
                intent.putExtra("id_responden", 4)
                startActivity(intent)
//            }
        }
    }

    fun getSpinnerData() {
        val call: Call<List<StakeholderType>> = Api().getInstance().getStakeholderType()
        call.enqueue(object : Callback<List<StakeholderType>> {
            override fun onFailure(call: Call<List<StakeholderType>>, t: Throwable) {
                logging("Cannot get Stakeholder Type: ${t.message}")
            }

            override fun onResponse(call: Call<List<StakeholderType>>, response: Response<List<StakeholderType>>) {
                listStakeholderType = response.body()
                populateSpinner()

            }

        })
    }

    private fun invalidateInputAndSend(): Int? {
        var check: Boolean = true
        var genderSelected: Int = -1
        var idResponden: Int? = -1

        check != TextUtils.isEmpty(name.text.toString())
        if (TextUtils.isEmpty(name.text.toString())) name.error = "Harus Diisi"

        check != TextUtils.isEmpty(address.text.toString())
        if (TextUtils.isEmpty(address.text.toString())) address.error = "Harus Diisi"

        check != TextUtils.isEmpty(phone.text.toString())
        if (TextUtils.isEmpty(phone.text.toString())) phone.error = "Harus Diisi"

        check != TextUtils.isEmpty(agency.text.toString())
        if (TextUtils.isEmpty(agency.text.toString())) agency.error = "Harus Diisi"

        gender.setOnCheckedChangeListener { _, i ->
            genderSelected = if (i == R.id.gender_man) 1
            else if (i == R.id.gender_woman) 2
            else -1

            check = (i == R.id.gender_man) || (i == R.id.gender_woman)
        }

//        check = stakeholdersSelected != -1 || stakeholdersSelected != 0

        println(check)
        if (check) {
            val data: RespondentData = RespondentData(
                name.text.toString(),
                address.text.toString(),
                genderSelected,
                phone.text.toString(),
                agency.text.toString(),
                stakeholderTypeSelected!!,
                stakeholdersSelected!!,
                residenceSelected
            )
            println(data)
            val call: Call<DataResponse> = Api().getInstance().sendDataRespondent(
                name.text.toString(),
                address.text.toString(),
                genderSelected,
                phone.text.toString(),
                agency.text.toString(),
                stakeholderTypeSelected!!,
                stakeholdersSelected!!,
                residenceSelected
            )
            call.enqueue(object : Callback<DataResponse> {
                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    logging("Cannot send data: ${t.message}")
                }

                override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                    idResponden = response.body()?.id_responden
                    println(" ${response.body()} dan $idResponden}")
                }

            })
        }

        return idResponden
    }

    private fun populateSpinner() {
        val lst: MutableList<String> = mutableListOf()
        listStakeholderType?.forEach {
            lst.add(it.nama_tipe)
        }

        ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, lst)
            .also {
                it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                stakeholders_type.adapter = it
            }

        ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, listOf("..."))
            .also {
                it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
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
                ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, st)
                    .also {
                        it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
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
