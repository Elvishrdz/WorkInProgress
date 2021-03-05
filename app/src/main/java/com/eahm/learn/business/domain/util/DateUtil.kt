package com.eahm.learn.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtil @Inject constructor(private val dateFormat : SimpleDateFormat){

    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    fun convertFirebaseTimestampToStringData(timestamp: Timestamp): String{
        return dateFormat.format(timestamp.toDate())
    }

    fun convertStringDateToFirebaseTimestamp(date: String): Timestamp {
        val dateObj : Date? = if(date.isNotEmpty()) dateFormat.parse(date) else null

        return dateObj?.let {
            Timestamp(it)
        } ?: Timestamp(Date())
    }

    fun getCurrentTimestamp(): String {
        return dateFormat.format(Date())
    }

}