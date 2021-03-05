package com.eahm.learn.utils

import android.util.Log
import com.eahm.learn.BuildConfig.DEBUG
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val TAG = "MyAppDebugging"
var isUnitTest = false

fun logD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

/*
    Priorities: Log.DEBUG, Log. etc....
 */
fun logCrashlytics(msg: String?){
    msg?.let {
        if(!DEBUG){
            FirebaseCrashlytics.getInstance().log(it)
        }
    }
}

fun <T> logList(className : String, list : List<T>){
    var text = ""
    for(element in list){
        text += "${element.toString()}\n"
    }
    Log.d(className, text)
}