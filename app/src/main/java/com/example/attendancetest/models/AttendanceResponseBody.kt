package com.example.attendancetest.models

data class AttendanceResponseBody(
    val attendance: Attendance,
    val month: String,
    val status: List<Statu>,
    val subject: Subject
)