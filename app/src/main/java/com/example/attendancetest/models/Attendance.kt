package com.example.attendancetest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Attendance(
    @SerializedName("attendance")
    val attendance: @RawValue HashMap<String, String>
): Parcelable