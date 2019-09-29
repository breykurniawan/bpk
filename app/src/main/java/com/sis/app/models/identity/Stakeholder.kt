package com.sis.app.models.identity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stakeholder(
    @SerializedName("id_pemangku_kepentingan") val id_pemangku_kepentingan: Int,
    @SerializedName("tipe") val tipe: Int,
    @SerializedName("nama") val nama: String
) : Parcelable