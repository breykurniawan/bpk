package com.sis.app.fragments


import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.sis.app.R
import com.sis.app.activities.LoginActivity
import com.sis.app.others.TinyDB

class Profile : Fragment() {

    private lateinit var headerImage: ImageView
    private lateinit var name: TextView
    private lateinit var email:TextView
    private lateinit var logout:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerImage = view.findViewById(R.id.header_cover_image)
        name = view.findViewById(R.id.user_profile_name)
        email = view.findViewById(R.id.user_profile_email)
        logout = view.findViewById(R.id.logout)

        headerImage.setImageBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.img_1)
        )
        logout.setOnClickListener {
            warnDialog()
        }
    }

    private fun warnDialog() {
        val dialog: AlertDialog.Builder? = AlertDialog.Builder(activity!!)
        dialog?.setMessage("Logout?")
            ?.setPositiveButton("Ya", { dialog, id ->
                TinyDB(context).putBoolean("isLoggedIn", false)
                startActivity(Intent(activity, LoginActivity::class.java))
                activity!!.finish()
            })
            ?.setNegativeButton("Tidak", { dialog, id ->
                dialog.dismiss()
            })
        dialog?.create()?.show()
    }
}
