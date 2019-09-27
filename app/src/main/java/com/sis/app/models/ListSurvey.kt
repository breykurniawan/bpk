package com.sis.app.models

import com.google.gson.annotations.SerializedName

data class ListSurvey(
    @SerializedName("survey") val survey: List<Survey>
)