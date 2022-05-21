package com.example.attendancetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.attendancetest.api.RetrofitApi
import com.example.attendancetest.models.LoginResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    private val TAG = "ReposeMainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitApi().login("7086020908", "7086020908")
            .enqueue(object: Callback<LoginResponseBody> {
                override fun onResponse(
                    call: Call<LoginResponseBody>,
                    response: Response<LoginResponseBody>
                ) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<LoginResponseBody>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.printStackTrace()}")
                }

            })
    }
}