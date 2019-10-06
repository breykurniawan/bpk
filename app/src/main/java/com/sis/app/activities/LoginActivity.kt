package com.sis.app.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.models.UserResponse
import com.sis.app.networks.Api
import com.sis.app.others.TinyDB
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            hideKeyboard()
            error_text.visibility = View.GONE
            error_text.text = ""
            if (invalidateInput()) {
                enabled(false)
                loggingIn()
            } else {
                enabled(true)
            }
        }
    }

    //function invoked when connected to the Internet


    private fun invalidateInput(): Boolean {
        var check1 = true
        var check2 = true

        if (TextUtils.isEmpty(username.text.toString())) {
            username.error = "Email Harus Diisi!"
            check1 = false
        }

        if (TextUtils.isEmpty(password.text.toString())) {
            password.error = "Kata Sandi Harus Diisi!"
            check2 = false
        }

        return check1 && check2
    }

    private fun loggingIn() {
        val call: Call<UserResponse> = Api().getInstance()
            .loginWithUsernamePassword(username.text.toString(), password.text.toString())
        call.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Snackbar.make(parent_layout, "Gagal Login", Snackbar.LENGTH_LONG).show()
                Log.w(LoginActivity::class.java.simpleName, "Gagal Login= ${t.message}")
                enabled(false)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()?.user != null && response.body()?.user?.role == 4) {
                            val db = TinyDB(applicationContext)
                            db.putInt("idSurveyor", response.body()?.user?.id ?: -1)
                            db.putString("namaSurveyor", response.body()?.user?.name)
                            db.putString("emailSurveyor", response.body()?.user?.email)
                            db.putBoolean("isLoggedIn", true)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        } else {
                            error_text.text = "Email atau Kata Sandi salah"
                            error_text.visibility = View.VISIBLE
                            enabled(true)
                        }
                    } else {
                        error_text.text = "Email atau Kata Sandi salah"
                        error_text.visibility = View.VISIBLE
                        enabled(true)
                    }
                } else {
                    Snackbar.make(parent_layout, "Gagal Login", Snackbar.LENGTH_LONG).show()
                    Log.w(
                        LoginActivity::class.java.simpleName,
                        "Gagal Login= ${response.message()}"
                    )
                    enabled(true)
                }

            }

        })
    }

    private fun enabled(bool: Boolean) {
        username.isEnabled = bool
        password.isEnabled = bool
        login.isEnabled = bool
        if (bool) {
            progress.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                parent_layout.foreground = null
            } else {
                overlay.visibility = View.GONE
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                parent_layout.foreground =
                    ColorDrawable(resources.getColor(R.color.grey_transparent))
            } else {
                overlay.visibility = View.VISIBLE
            }
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}