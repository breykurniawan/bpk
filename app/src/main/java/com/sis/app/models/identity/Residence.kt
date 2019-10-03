package com.sis.app.models.identity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Residence(
    @SerializedName("id_domisili") val id_domisili: Int,
    @SerializedName("provinsi") val provinsi: String
) : Parcelable