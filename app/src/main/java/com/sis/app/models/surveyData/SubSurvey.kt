package com.sis.app.models.surveyData

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubSurvey(
    @SerializedName("id_bagian") val id_bagian: Int,
    @SerializedName("id_kuisioner") val id_kuisioner: Int,
    @SerializedName("id_sub_bagian") val id_sub_bagian: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("subs") val subs: List<Subs>
) :Parcelable