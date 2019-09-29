package com.sis.app.models.identity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RespondentData(
    @SerializedName("nama") val nama: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("jenis_kelamin") val jenis_kelamin: Int,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("nama_instansi") val nama_instansi: String,
    @SerializedName("tipe_stakeholder") val tipe_stakeholder: Int?,
    @SerializedName("nama_stakeholder") val nama_stakeholder: Int?,
    @SerializedName("domisili") val domisili: Int
) : Parcelable