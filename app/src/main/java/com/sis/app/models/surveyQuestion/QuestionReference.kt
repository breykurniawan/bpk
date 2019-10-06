package com.sis.app.models.surveyQuestion

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionReference(
    val id_pertanyaan: Int,
    val id_bagian: Int,
    val judul: String,
    val tipe: String,
    var nilai: String?,
    val nomor: String
) : Parcelable