package com.example.attendancetest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class AttendanceResponseBody(
    @SerializedName("month")
    val month: String,
    @SerializedName("subject")
    val subject: @RawValue Subject,
    @SerializedName("attendance")
    val attendance: @RawValue Map<String, String>,
    @SerializedName("status")
    val status: @RawValue List<Statu>,

    ): Parcelable