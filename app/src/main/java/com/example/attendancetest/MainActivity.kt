package com.example.attendancetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.attendancetest.api.RetrofitApi
import com.example.attendancetest.databinding.ActivityMainBinding
import com.example.attendancetest.models.LoginResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    private val TAG = "ReposeMainActivity"
    private lateinit var binding: ActivityMainBinding
    private var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.logIn.setOnClickListener {
            var username = binding.usernameTextInput.text.toString()
            var password = binding.passwordTextInput.text.toString()
            login(username, password)
        }


    }

    private fun login(username: String, password: String) {
        RetrofitApi().login(username, password)
            .enqueue(object: Callback<LoginResponseBody> {
                override fun onResponse(
                    call: Call<LoginResponseBody>,
                    response: Response<LoginResponseBody>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Enter Proper Details", Toast.LENGTH_SHORT).show()
                    }
                    Log.d(TAG, "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<LoginResponseBody>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.printStackTrace()}")
                    Toast.makeText(applicationContext, "Server error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}