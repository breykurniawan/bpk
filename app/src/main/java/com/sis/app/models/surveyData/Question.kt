package com.sis.app.models.surveyData

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    @SerializedName("id_pertanyaan") val id_pertanyaan: Int,
    @SerializedName("id_bagian") val id_bagian: Int,
    @SerializedName("pertanyaan") val pertanyaan: String
) : Parcelable