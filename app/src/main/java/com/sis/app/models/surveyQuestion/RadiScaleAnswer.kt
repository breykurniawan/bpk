package com.sis.app.models.surveyQuestion

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioScaleAnswer(
    @SerializedName("id_pertanyaan") val id_pertanyaan: Int,
    @SerializedName("tipe") val tipe:String,
    @SerializedName("nilai") var nilai: Int
) : Parcelable