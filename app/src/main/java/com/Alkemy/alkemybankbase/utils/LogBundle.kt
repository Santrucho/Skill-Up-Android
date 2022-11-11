package com.Alkemy.alkemybankbase.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object LogBundle {

    fun logBundleAnalytics(firebaseAnalytics:FirebaseAnalytics,message:String,eventName:String){
        var bundle = Bundle()
        bundle.putString("message", message)
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}