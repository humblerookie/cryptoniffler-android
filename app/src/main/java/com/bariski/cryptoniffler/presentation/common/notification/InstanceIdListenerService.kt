/*
 * Copyright (c) 2015, Craftsvilla Handicrafts Private Limited.
 * Written under contract by First Quadrant Labs Pvt. Ltd.
 */

package com.bariski.cryptoniffler.presentation.common.notification

/**
 * Created by Admin on 9/12/2015.
 */

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber


class InstanceIdListenerService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Timber.d("Refreshed token:  %s", refreshedToken)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(FirebaseInstanceId.getInstance().id, refreshedToken)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.ARBITRAGE_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.NEWS_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.ICO_ID)
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(id: String?, token: String?) {

    }

    companion object {

        private val TAG = "MyFirebaseIIDService"
    }
}