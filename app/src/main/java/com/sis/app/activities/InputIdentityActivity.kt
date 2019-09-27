package com.sis.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.widget.ArrayAdapter
import com.sis.app.R
import com.sis.app.models.SubSurvey
import kotlinx.android.synthetic.main.activity_input_identity.*
import java.util.ArrayList

class InputIdentityActivity : AppCompatActivity() {

    private var stakeholdersSelected: Int = -1
    private var residenceSelected: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_identity)
        val model: List<SubSurvey>? = intent.getParcelableArrayListExtra("model")

        ArrayAdapter.createFromResource(
            this, R.array.pemangku_kepentingan, R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            stakeholders.adapter = it
        }
        ArrayAdapter.createFromResource(
            this, R.array.domisili, R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            residence.adapter = it
        }

//        stakeholders.setOnItemClickListener { _, _, pos, _ ->
//            stakeholdersSelected = pos
//        }
//
//
//        residence.setOnItemClickListener{ _, _, pos, _ ->
//            residenceSelected = pos
//        }

        next.setOnClickListener {
            val intent = Intent(this, SubSurveySection::class.java)
            intent.putParcelableArrayListExtra("model", model as ArrayList<out Parcelable>)
            if (invalidateInput()) startActivity(intent)
        }
    }

    private fun invalidateInput(): Boolean {
        var check: Boolean

        check = if (TextUtils.isEmpty(name.text.toString())) {
            name.error = "Harus Diisi"
            false
        } else true

        check = if (TextUtils.isEmpty(address.text.toString())) {
            address.error = "Harus Diisi"
            false
        } else true

        check = if (TextUtils.isEmpty(phone.text.toString())) {
            phone.error = "Harus Diisi"
            false
        } else true

        check = if (TextUtils.isEmpty(agency.text.toString())) {
            agency.error = "Harus Diisi"
            false
        } else true

        gender.setOnCheckedChangeListener { _, i ->
            check = (i == R.id.gender_man) || (i == R.id.gender_woman)
//            check = if (i == R.id.gender_man) true
//            else i == R.id.gender_woman
        }

        check = stakeholdersSelected != -1 || stakeholdersSelected != 0

        return check
    }
}
