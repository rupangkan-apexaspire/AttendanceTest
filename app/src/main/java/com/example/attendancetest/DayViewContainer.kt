package com.example.attendancetest

import android.view.View
import com.example.attendancetest.databinding.Example1CalendarDayBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    // Will be set when this container is bound. See the dayBinder.
    lateinit var day: CalendarDay
    val textView = Example1CalendarDayBinding.bind(view).exOneDayText

//    init {
//        view.setOnClickListener {
////            if (day.owner == DayOwner.THIS_MONTH) {
////                if (selectedDates.contains(day.date)) {
////                    selectedDates.remove(day.date)
////                } else {
////                    selectedDates.add(day.date)
////                }
////                binding.exOneCalendar.notifyDayChanged(day)
////            }
//        }
//    }
}