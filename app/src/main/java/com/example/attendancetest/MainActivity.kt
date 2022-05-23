package com.example.attendancetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.attendancetest.api.RetrofitApi
import com.example.attendancetest.databinding.ActivityMainBinding
import com.example.attendancetest.models.LoginResponseBody
import com.example.attendancetest.session.LoginPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    private val TAG = "ReposeMainActivity"
    private lateinit var binding: ActivityMainBinding
    private var username = ""
    private var password = ""
    private lateinit var loginResponse: LoginResponseBody
    private lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = LoginPref(this)
        if(session.isLoggedIn()) {
            var intent = Intent(applicationContext, CalenderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

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
                        loginResponse = response.body()!!
                        var intent = Intent(this@MainActivity, CalenderActivity::class.java)
                        session.createLoginSession(binding.usernameTextInput.text.toString(), binding.passwordTextInput.text.toString(), loginResponse.access)
//                        intent.putExtra("token", loginResponse.access)
                        startActivity(intent)
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