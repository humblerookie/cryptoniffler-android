package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.domain.util.PLATFORM
import com.bariski.cryptoniffler.domain.util.VERSION
import com.squareup.moshi.Json

data class Device(@Json(name = "firebaseInstanceId") val instanceId: String
                  , @Json(name = "fcmPushAddress") val fcmToken: String
                  , @Json(name = "appVersion") val version: String = BuildConfig.VERSION_NAME
                  , @Json(name = "appVersionCode") val versionCode: String = "$VERSION"
                  , @Json(name = "platform") val platform: String = PLATFORM
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(instanceId)
        parcel.writeString(fcmToken)
        parcel.writeString(version)
        parcel.writeString(versionCode)
        parcel.writeString(platform)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Device> {
        override fun createFromParcel(parcel: Parcel): Device {
            return Device(parcel)
        }

        override fun newArray(size: Int): Array<Device?> {
            return arrayOfNulls(size)
        }
    }
}