package com.bariski.cryptoniffler.data.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


fun getAssetFromDevice(fileName: String, context: Context): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(
                InputStreamReader(context.assets.open(fileName)))

        // do reading, usually loop until end of file reading
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        return sb.toString()
    } catch (e: IOException) {
        Log.d("DeviceUtil", e.toString())
    } finally {
        if (reader != null) {
            try {
                reader.close()
            } catch (e: IOException) {
                Log.d("DeviceUtil", e.toString())
            }
        }
    }
    return null
}