package com.sis.app.models.surveyData

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @SerializedName("data") val data: List<ListSurvey>
) : Parcelable


@Parcelize
data class ListSurvey(
    @SerializedName("id_kuisioner") val id_kuisioner: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("tipe_responden") val tipe_responden: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) : Parcelable