package com.example.attendancetest.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.attendancetest.CalenderActivity
import com.example.attendancetest.MainActivity

class LoginPref {
    var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    var con: Context
    private var PRIVATEMODE: Int = 0

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()
    }

    companion object {
        const val PREF_NAME = "Login_Preference"
        const val IS_LOGIN = "isLoggedIn"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val ACCESS_TOKEN = "token"
    }

    fun createLoginSession(userName: String, password: String, token: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, userName)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(ACCESS_TOKEN, token)
        editor.commit()
    }

    fun checkLogin() {
        if(!this.isLoggedIn()) {
            val i: Intent = Intent(con, CalenderActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String> {
        var user: Map<String, String> = HashMap<String, String>()
        (user as HashMap)[KEY_USERNAME] = pref.getString(KEY_USERNAME, null)!!
        (user as HashMap)[KEY_PASSWORD] = pref.getString(KEY_PASSWORD, null)!!
        (user as HashMap)[ACCESS_TOKEN] = pref.getString(ACCESS_TOKEN, null)!!
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        var i: Intent = Intent(con, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}