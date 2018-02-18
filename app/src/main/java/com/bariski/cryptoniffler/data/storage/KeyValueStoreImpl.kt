package com.bariski.cryptoniffler.data.storage

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyValueStoreImpl @Inject constructor(context: Context) : KeyValueStore {

    private var pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()


    override fun storeString(key: String, `val`: String): Boolean {
        return editor.putString(key, `val`).commit()
    }

    override fun storeBoolean(key: String, `val`: Boolean): Boolean {
        return editor.putBoolean(key, `val`).commit()
    }

    override fun storeInt(key: String, `val`: Int): Boolean {
        return editor.putInt(key, `val`).commit()
    }

    override fun storeLong(key: String, `val`: Long): Boolean {
        return editor.putLong(key, `val`).commit()
    }

    override fun storeFloat(key: String, `val`: Float): Boolean {
        return editor.putFloat(key, `val`).commit()
    }

    override fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    override fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    override fun getInt(key: String): Int {
        return pref.getInt(key, 0)
    }

    override fun getLong(key: String): Long {
        return pref.getLong(key, 0)
    }

    override fun getFloat(key: String): Float {
        return pref.getFloat(key, 0f)
    }
}