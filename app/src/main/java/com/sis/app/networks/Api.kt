package com.sis.app.networks

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import com.sis.app.R
import com.sis.app.models.UserResponse
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.identity.Residence
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.identity.StakeholderType
import com.sis.app.models.surveyData.ListSurvey
import com.sis.app.models.surveyData.RespondentSurveyData
import com.sis.app.models.surveyQuestion.Answer
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.util.concurrent.TimeUnit

class Api {

    companion object {
        private const val BASE_URL: String = "https://bpk.wdu-survey.com/api/"
    }

    private fun retrofit(): Retrofit {
        var interceptor = HttpLoggingInterceptor()
        val client =
            OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getInstance(): ApiInterface {
        return retrofit().create(ApiInterface::class.java)
    }

    //TODO lengkapi alamat-alamat endpoint
    interface ApiInterface {
        @GET("kuisioner/{id_kuisioner}")
        fun getQuestionnaire(@Path("id_kuisioner") id_kuisioner: Int): Call<Survey>

        @GET("tipePemangkuKepentingan")
        fun getStakeholderType(): Call<List<StakeholderType>>

        @GET("kuisioner")
        fun getListKuisioner(): Call<List<ListSurvey>>

        @GET("respondenBySurveyor/{id_surveyor}")
        fun getListRespondent(@Path("id_surveyor") id_surveyor: Int): Call<List<RespondentSurveyData>>

        @GET("domisili")
        fun getResidence(): Call<List<Residence>>

        @FormUrlEncoded
        @POST("responden/submit")
        fun sendDataRespondent(
            @Field("nama") nama: String,
            @Field("alamat") alamat: String,
            @Field("jenis_kelamin") jenis_kelamin: Int,
            @Field("no_hp") no_hp: String,
            @Field("nama_instansi") nama_instansi: String,
            @Field("tipe_stakeholder") tipe_stakeholder: Int,
            @Field("nama_stakeholder") nama_stakeholder: Int,
            @Field("domisili") domisili: Int,
            @Field("usia") usia: String,
            @Field("pendidikan") pendidikan: String
        ): Call<DataResponse>

        @POST("jawaban/submit")
        fun sendAnswer(
            @Body jawaban: Answer
        ): Call<ResponseBody>

        @FormUrlEncoded
        @POST("login")
        fun loginWithUsernamePassword(@Field("email") email: String, @Field("password") password: String): Call<UserResponse>

        @Multipart
        @POST("jawabanImage/submit")
        fun sendImageRespondent(
            @Part foto: MultipartBody.Part,
            @PartMap data :Map<String, Int>
        ): Call<ResponseBody>
    }
}

