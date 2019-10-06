package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.models.identity.Residence
import com.sis.app.models.identity.RespondentData
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
    private var residenceSelected: Int? = -1
    private var ageSelected: String = ""
    private var educationSelected: String = ""
    private var genderSelected: Int = -1

    private var tipeStakeholder = -1

    private var listStakeholderType: List<StakeholderType>? = listOf()
    private var listStakeholder: List<Stakeholder>? = listOf()
    private var listResidence: List<Residence>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_identity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val model: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")
        val id_kuisioner: Int = intent.getIntExtra("id_kuisioner", -1)
        tipeStakeholder = intent.getIntExtra("id_tipe_pemangku", -1)

        stakeholders_type.isEnabled = false
        getSpinnerData()

        gender.setOnCheckedChangeListener { _, i ->
            if (i == R.id.gender_man) {
                genderSelected = 1
            } else if (i == R.id.gender_woman) {
                genderSelected = 2
            }
        }

        next.setOnClickListener {
            invalidateInputAndSend(id_kuisioner);
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name.text.toString())
        outState.putString("address", name.text.toString())
        outState.putString("phone", name.text.toString())
        outState.putString("agency", name.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.setText(savedInstanceState.getString("name"), TextView.BufferType.EDITABLE)
        name.setText(savedInstanceState.getString("address"), TextView.BufferType.EDITABLE)
        name.setText(savedInstanceState.getString("phone"), TextView.BufferType.EDITABLE)
        name.setText(savedInstanceState.getString("agency"), TextView.BufferType.EDITABLE)
    }

    private fun getSpinnerData() {
        val call1: Call<List<StakeholderType>> = Api().getInstance().getStakeholderType()
        val call2: Call<List<Residence>> = Api().getInstance().getResidence()
        call1.enqueue(object : Callback<List<StakeholderType>> {
            override fun onFailure(call: Call<List<StakeholderType>>, t: Throwable) {
                Snackbar.make(
                    parent_layout,
                    "Gagal Menerima Data Pemangku Kepentingan",
                    Snackbar.LENGTH_LONG
                ).show()
                next.isEnabled = false
                logging("Cannot get Stakeholder Type: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<StakeholderType>>, response: Response<List<StakeholderType>>
            ) {
                if (response.isSuccessful) {
                    listStakeholderType = response.body()
                    populateSpinner()
                } else {
                    Snackbar.make(
                        parent_layout,
                        "Gagal Menerima Data Pemangku Kepentingan\n${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    logging("Gagal Menerima Data\n${response.message()}")
                }
            }
        })
        call2.enqueue(object : Callback<List<Residence>> {
            override fun onFailure(call: Call<List<Residence>>, t: Throwable) {
                Snackbar.make(parent_layout, "Gagal Menerima Data Domisili", Snackbar.LENGTH_LONG)
                    .show()
                next.isEnabled = false
                logging("Cannot get Residence: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<Residence>>,
                response: Response<List<Residence>>
            ) {
                if (response.isSuccessful) {
                    listResidence = response.body()
                    populateSpinnerResidence()
                } else {
                    Snackbar.make(
                        parent_layout,
                        "Gagal Menerima Data Pemangku Kepentingan\n${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    logging("Gagal Menerima Data Pemangku Kepentingan\n${response.message()}")
                }
            }

        })

    }

    private fun invalidateInputAndSend(id_kuisioner: Int) {
        var check: Boolean = true

        check = true != TextUtils.isEmpty(name.text.toString())
        if (TextUtils.isEmpty(name.text.toString())) name.error = "Harus Diisi"

        check = true != TextUtils.isEmpty(address.text.toString())
        if (TextUtils.isEmpty(address.text.toString())) address.error = "Harus Diisi"

        check = true != TextUtils.isEmpty(phone.text.toString())
        if (TextUtils.isEmpty(phone.text.toString())) phone.error = "Harus Diisi"
        if (stakeholders_type.selectedItem.toString().equals("MASYARAKAT")) {

        } else {
            check = true != TextUtils.isEmpty(agency.text.toString())
            if (TextUtils.isEmpty(agency.text.toString())) agency.error = "Harus Diisi"
        }
        if (check) {
            var data: RespondentData?
            if (stakeholders_type.selectedItem.toString().equals("MASYARAKAT")) {
                data = RespondentData(
                    name.text.toString(),
                    address.text.toString(),
                    genderSelected,
                    phone.text.toString(),
                    "kosong",
                    stakeholderTypeSelected!!,
                    stakeholdersSelected!!,
                    residenceSelected,
                    ageSelected,
                    educationSelected
                )
            } else {
                data = RespondentData(
                    name.text.toString(), address.text.toString(), genderSelected,
                    phone.text.toString(), agency.text.toString(),
                    stakeholderTypeSelected!!, stakeholdersSelected!!, residenceSelected, "kosong", "kosong"
                )
            }
            println("data responden = $data")
            val intent = Intent(this@InputIdentityActivity, GuideActivity::class.java)
            intent.putExtra("id_kuisioner", id_kuisioner)
            intent.putExtra("respondenData", data)
            if (stakeholders_type.selectedItem.toString().equals("MASYARAKAT", true)) {
                intent.putExtra(("masyarakat"), true)
            }
            overlay.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
            Handler().postDelayed({
                startActivity(intent)
            }, 2000)
        }
    }

    private fun populateSpinnerResidence() {
        val lst: MutableList<String> = mutableListOf()
        listResidence?.forEach {
            lst.add(it.provinsi)
        }

        ArrayAdapter(applicationContext, R.layout.spinner_item, lst).also {
            it.setDropDownViewResource(R.layout.spinner_item)
            residence.adapter = it
        }

        residence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                residenceSelected = listResidence?.get(pos)?.id_domisili
            }
        }
    }

    private fun populateSpinner() {
        val lst: MutableList<String> = mutableListOf()
        listStakeholderType?.forEach {
            lst.add(it.nama_tipe)
        }

        ArrayAdapter(applicationContext, R.layout.spinner_item, lst).also {
            it.setDropDownViewResource(R.layout.spinner_item)
            stakeholders_type.adapter = it
        }

        ArrayAdapter(applicationContext, R.layout.spinner_item, listOf("...")).also {
            it.setDropDownViewResource(R.layout.spinner_item)
            stakeholders.adapter = it
        }

        ArrayAdapter.createFromResource(applicationContext, R.array.usia, R.layout.spinner_item)
            .also {
                it.setDropDownViewResource(R.layout.spinner_item)
                age.adapter = it
            }

        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.pendidikan,
            R.layout.spinner_item
        ).also {
            it.setDropDownViewResource(R.layout.spinner_item)
            education.adapter = it
        }

        stakeholders_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                val watcher = NumberWatcher()
                if (stakeholders_type.selectedItem.toString().equals("MASYARAKAT", true)) {
                    text_age.visibility = View.VISIBLE
                    text_education.visibility = View.VISIBLE
                    age.visibility = View.VISIBLE
                    education.visibility = View.VISIBLE
                    agency.visibility = View.GONE
                    text_stakeholders.text = "Jenis Responden"
//                    text_stakeholders.text = "Pendidikan"
//                    layout_agency.hint = "Usia"
//                    agency.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL

                    stakeholderTypeSelected = listStakeholderType?.get(pos)?.id
                    listStakeholderType?.get(pos)?.id
                    listStakeholder = listStakeholderType?.get(pos)?.pemangku_kepentingan
                    val st: MutableList<String> = mutableListOf()
                    listStakeholder?.forEach {
                        st.add(it.nama)
                    }
                    ArrayAdapter(applicationContext, R.layout.spinner_item, st).also {
                        it.setDropDownViewResource(R.layout.spinner_item)
                        stakeholders.adapter = it
                    }
                } else {
                    text_age.visibility = View.GONE
                    text_education.visibility = View.GONE
                    age.visibility = View.GONE
                    education.visibility = View.GONE
                    agency.visibility = View.VISIBLE
//                    text_stakeholders.text = "Pemangku Kepentingan"
//                    layout_agency.hint = "Instansi"
//                    agency.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_TEXT_FLAG_CAP_WORDS
//                    agency.removeTextChangedListener(watcher)
                    stakeholderTypeSelected = listStakeholderType?.get(pos)?.id
                    listStakeholderType?.get(pos)?.id
                    listStakeholder = listStakeholderType?.get(pos)?.pemangku_kepentingan
                    val st: MutableList<String> = mutableListOf()
                    listStakeholder?.forEach {
                        st.add(it.nama)
                    }
                    ArrayAdapter(applicationContext, R.layout.spinner_item, st).also {
                        it.setDropDownViewResource(R.layout.spinner_item)
                        stakeholders.adapter = it
                    }
                }
            }

        }

        listStakeholderType?.forEachIndexed { index, stakeholderType ->
            if (stakeholderType.id == tipeStakeholder) {
                stakeholders_type.setSelection(index)
            }
        }

        stakeholders.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                stakeholdersSelected = listStakeholder?.get(pos)?.id_pemangku_kepentingan
            }

        }

        age.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ageSelected = age.selectedItem.toString()
            }

        }

        education.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                educationSelected = education.selectedItem.toString()
            }

        }
    }

    private fun logging(msg: String) = Log.w(InputIdentityActivity::class.java.simpleName, msg)
}
