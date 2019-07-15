package com.maxlab.receiptapp.util

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE



class UtilSharedPreferences {


    private var sharedPreferences : SharedPreferences? = null


    constructor(sharedPreferences: SharedPreferences?) {
        this.sharedPreferences = sharedPreferences
    }

    companion object  {

        @Volatile private var instance: UtilSharedPreferences? = null
        private var sharedPreferences: SharedPreferences? = null
        private var tagKey = "UtilSharedPreferences"

        fun getInstance(context: Context, tag : String = tagKey) : UtilSharedPreferences? {

            if (context != null) {
                sharedPreferences = context.getSharedPreferences(tag, MODE_PRIVATE)
                instance = UtilSharedPreferences(sharedPreferences)
            }
            return instance
        }

    }

    fun setIsFirsTime(boolean: Boolean) {
        saveData("isFirstTime",boolean.toString())
    }

    fun isFirstTime() : Boolean{
        val result = getData("isFirstTime")
        if(result == ""){
            return true
        }
        return result!!.toBoolean()
    }



    fun saveData(key: String, value: String) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getData(key: String): String? {
        return if (sharedPreferences != null) {
            sharedPreferences!!.getString(key, "")
        } else ""
    }
}
