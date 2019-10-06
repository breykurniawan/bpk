package com.sis.app.models.surveyQuestion

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerModel(
    @SerializedName("id_pertanyaan") val id_pertanyaan: Int,
    @SerializedName("id_responden") val id_responden: Int? = -1,
    @SerializedName("id_user") val id_user: Int? = -1,
    @SerializedName("nilai") var nilai: String? = ""
//    @SerializedName("nilai_teks") var nilai_teks: String? = ""
) : Parcelable

data class Answer(
    @SerializedName("jawaban") val jawaban: List<AnswerModel>
)