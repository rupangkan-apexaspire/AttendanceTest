package com.example.attendancetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.children
import com.example.attendancetest.api.RetrofitApi
import com.example.attendancetest.databinding.ActivityCalenderBinding
import com.example.attendancetest.models.Attendance
import com.example.attendancetest.models.AttendanceResponseBody
import com.example.attendancetest.session.LoginPref
import com.example.attendancetest.utils.daysOfWeekFromLocale
import com.example.attendancetest.utils.setTextColorRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private lateinit var session: LoginPref
    private lateinit var attendanceResponse: AttendanceResponseBody
    private lateinit var attendance: Attendance
    private var hashMap : HashMap<String, String> = HashMap<String, String> ()
    private val TAG = "ReposeCalenderActivity"
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = LoginPref(this)
        var userDetail = session.getUserDetails()
        var token = userDetail["token"]
//        var token = intent.extras?.get("token")
        binding.logout.setOnClickListener{
            session.logoutUser()
        }
        Log.d(TAG, "onCreate: $token")
        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.root.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(
                    Locale.ENGLISH
                )
                setTextColorRes(R.color.example_1_white_light)
            }
        }
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        binding.exOneCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.exOneCalendar.scrollToMonth(currentMonth)

//        class DayViewContainer(view: View) : ViewContainer(view) {
//            // Will be set when this container is bound. See the dayBinder.
//            lateinit var day: CalendarDay
//            val textView = Example1CalendarDayBinding.bind(view).exOneDayText
//
////            init {
////                view.setOnClickListener {
////                    if (day.owner == DayOwner.THIS_MONTH) {
////                        if (selectedDates.contains(day.date)) {
////                            selectedDates.remove(day.date)
////                        } else {
////                            selectedDates.add(day.date)
////                        }
////                        binding.exOneCalendar.notifyDayChanged(day)
////                    }
////                }
////            }
//        }

        binding.exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
//                var date = ""
//                date = if(day.date.monthValue<10) {
//                    "0"+day.date.monthValue
//                } else {
//                    day.date.monthValue.toString()
//                }
//                date += "-${day.date.year}"

//                Log.d(TAG, "bind: $day ${DayOwner.THIS_MONTH} $date")
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
            }
        }

        binding.exOneCalendar.monthScrollListener = {
            if (binding.exOneCalendar.maxRowCount == 6) {
                binding.exOneYearText.text = it.yearMonth.year.toString()
                binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)

                var date = ""
                date = if(it.month<10) {
                    "0"+it.month
                } else {
                    it.month.toString()
                }
                date += "-${it.yearMonth.year}"

                Log.d(TAG, "onCreate: $date")

                getResponse(date, token)
                
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                    binding.exOneMonthText.text = monthTitleFormatter.format(firstDate)
                } else {
                    binding.exOneMonthText.text =
                        "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                    if (firstDate.year == lastDate.year) {
                        binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.exOneYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }
    }

    private fun getResponse(date: String, token: String?) {
        Log.d(TAG, "getResponse: Bearer $token")
        try {
            RetrofitApi().getAttendance(date, "Bearer $token")
                .enqueue(object : Callback<AttendanceResponseBody> {
                    override fun onResponse(
                        call: Call<AttendanceResponseBody>,
                        response: Response<AttendanceResponseBody>
                    ) {
                        Log.d(TAG, "onResponse: ${response.body()}")
                        attendanceResponse = response.body()!!
//                        attendance = response.body()!!.attendance
                        Log.d(TAG, "onResponse: ${attendanceResponse.attendance}")

//                        var attendanceObj = response.body()!!.attendance.toString()
//                        var jsonObj = JSONObject(attendanceObj)
//                        var jsonArray = jsonObj.optJSONArray("attendance")

                        bindResponse()
                    }

                    override fun onFailure(call: Call<AttendanceResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to Get Response", Toast.LENGTH_SHORT)
        }

    }

    private fun bindResponse() {
        binding.exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day

//                Log.d(TAG, "bind: $day ${DayOwner.THIS_MONTH} $date")
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    val value = attendanceResponse.attendance[day.date.dayOfMonth.toString()]
                    for (i in 0 until attendanceResponse.status.size) {
                        if(attendanceResponse.status[i].id == value) {
                            var color = attendanceResponse.status[i].status_color.toColorInt()
                            textView.setBackgroundColor(color)
                        }
                    }
//                    attendanceResponse.attendance.forEach{
//                        when {
//                            it.key.equals(day.date.dayOfMonth) -> {
//                                Log.d(TAG, "bind: ${it.key} ${day.date.dayOfMonth}")
//                                textView.setTextColorRes(R.color.example_1_bg)
//                                for(i in 0..attendanceResponse.status.size){
//                                    if(attendanceResponse.status[i].id == it.value) {
//                                        Log.d(TAG, "bindAttendance: ${attendanceResponse.status[i].id} ${it.key} ${it.value}")
//                                        var color = attendanceResponse.status[i].status_color.toColorInt()
//                                        textView.setBackgroundColor(color)
//
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                    when {
//                        selectedDates.contains(day.date) -> {
//                            textView.setTextColorRes(R.color.example_1_bg)
//                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
//                        }
//                        today == day.date -> {
//                            textView.setTextColorRes(R.color.example_1_white)
//                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
//                        }
//                        else -> {
//                            textView.setTextColorRes(R.color.example_1_white)
//                            textView.background = null
//                        }
//                    }
                    when {
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
            }
        }
    }
}