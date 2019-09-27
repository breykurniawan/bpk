package com.sis.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("question_type") val question_type: String,
    @SerializedName("option_name") val option_name: List<String>? = listOf()
) : Parcelable