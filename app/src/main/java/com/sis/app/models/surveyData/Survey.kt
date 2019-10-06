package com.sis.app.models.surveyData

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(
    @SerializedName("id_kuisioner") val id_kuisioner: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("tipe_responden") val tipe_responden: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("bagian") val bagian: List<SubSurvey>
) : Parcelable


@Parcelize
data class SubSurvey(
    @SerializedName("id_bagian") val id_bagian: Int,
    @SerializedName("id_kuisioner") val id_kuisioner: Int,
    @SerializedName("id_sub_bagian") val id_sub_bagian: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("subs") val subs: List<Subs>
) :Parcelable

@Parcelize
data class Subs(
    @SerializedName("id_bagian") val id_bagian: Int,
    @SerializedName("id_kuisioner") val id_kuisioner: Int,
    @SerializedName("id_sub_bagian") val id_sub_bagian: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("pertanyaan") val pertanyaan: List<Question>
) : Parcelable

@Parcelize
data class Question(
    @SerializedName("id_pertanyaan") val id_pertanyaan: Int,
    @SerializedName("id_bagian") val id_bagian: Int,
    @SerializedName("tipe") val tipe: String,
    @SerializedName("pertanyaan") val pertanyaan: String
) : Parcelable