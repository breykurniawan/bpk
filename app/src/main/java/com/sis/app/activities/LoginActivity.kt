package com.sis.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sis.app.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            error_text.visibility = View.GONE
            overlay.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
            Handler().postDelayed({
                if (invalidateInput()) startActivity(Intent(this, MainActivity::class.java))
            }, 2000)
            overlay.visibility = View.GONE
            progress.visibility = View.GONE
        }
    }

    private fun invalidateInput(): Boolean {
        var check = true

        check != TextUtils.isEmpty(username.text.toString())
        if (TextUtils.isEmpty(username.text.toString())) username.error = "Nama Harus Diisi!"

        check != (TextUtils.isEmpty(password.text.toString()))
        if (TextUtils.isEmpty(password.text.toString())) password.error = "Kata Sandi Harus Diisi!"

        check = username.text.toString().equals("surveyor") && password.text.toString().equals("surveyor")
        if (!username.text.toString().equals("surveyor") || !password.text.toString().equals("surveyor")) {
            error_text.text = "Nama Atau Kata Sandi Salah"
            error_text.visibility = View.VISIBLE
        }

        return check
    }
}