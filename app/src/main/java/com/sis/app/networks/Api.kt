package com.sis.app.networks

import com.sis.app.models.identity.RespondentData
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.identity.StakeholderType
import com.sis.app.models.surveyData.SubSurvey
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class Api {

    companion object {
        private const val BASE_URL: String = "https://jejakhosting.com/api/"
    }

    private fun retrofit(): Retrofit {
        var interceptor = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(15, TimeUnit.SECONDS)
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
        fun getQuestioner(@Path("id_kuisioner") id_kuisioner: Int): Call<Survey>

        @GET("tipePemangkuKepentingan")
        fun getStakeholderType(): Call<List<StakeholderType>>

        /**
         * nama: String,

         */
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
            @Field("domisili") domisili: Int
        ): Call<DataResponse>

//        @POST("/kuisioner/login")
//        fun login(@Path("") )
    }
}