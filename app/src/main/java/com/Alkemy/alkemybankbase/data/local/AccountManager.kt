package com.Alkemy.alkemybankbase.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Alkemy.alkemybankbase.R
import kotlin.properties.Delegates

object AccountManager {

    var userId : Int = 0
    var accountId : Int = 0
    var balance : Int = 0

    fun saveIds(context: Context, valueUser: String,valueAccount:String) {
        val prefs : SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("userId",valueUser)
        editor.putString("accountId",valueAccount)
        editor.apply()
    }

    fun getUserId(context: Context): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString("userId",null)
    }
    fun getAccountId(context: Context): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString("accountId", null)
    }

}