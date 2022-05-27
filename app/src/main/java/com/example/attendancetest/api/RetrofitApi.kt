package com.example.attendancetest.api

import com.example.attendancetest.models.AttendanceResponseBody
import com.example.attendancetest.models.LoginResponseBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitApi {

    @FormUrlEncoded
    @POST("login/")
    fun login(@Field("username") username: String, @Field("password") password: String ) : Call<LoginResponseBody>

    @GET("attendance/8176/{date}/day")
    fun getAttendance(@Path(value = "date") date: String, @Header("Authorization") auth: String) : Call<AttendanceResponseBody>


    companion object {

        private val interceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor) // same for .addInterceptor(...)
            .build()


        operator fun invoke(): RetrofitApi {
            return Retrofit.Builder()
                .baseUrl("http://api.dev2.infotute.com/infotute/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitApi::class.java)
        }
    }
}