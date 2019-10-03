package com.sis.app.models.surveyData

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RespondentSurveyData(
    @SerializedName("nama_responden") val nama_responden: String,
    @SerializedName("pemangku_kepentingan") val pemangku_kepentingan: String,
    @SerializedName("created_at") val created_at: String
) : Parcelable