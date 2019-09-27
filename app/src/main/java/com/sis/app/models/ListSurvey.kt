package com.sis.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListSurvey(
    @SerializedName("survey") val survey: List<Survey>
) : Parcelable