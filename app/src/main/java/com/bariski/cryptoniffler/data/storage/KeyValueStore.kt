package com.bariski.cryptoniffler.data.storage

interface KeyValueStore {

    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int
    fun getLong(key: String): Long
    fun getFloat(key: String): Float

    fun storeString(key: String, `val`: String): Boolean
    fun storeBoolean(key: String, `val`: Boolean): Boolean
    fun storeInt(key: String, `val`: Int): Boolean
    fun storeLong(key: String, `val`: Long): Boolean
    fun storeFloat(key: String, `val`: Float): Boolean
}