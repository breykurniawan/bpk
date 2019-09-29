package com.sis.app.models.identity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StakeholderType(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_tipe") val nama_tipe: String,
    @SerializedName("pemangku_kepentingan") val pemangku_kepentingan: List<Stakeholder>
) : Parcelable