package com.example.attendancetest.adapter

import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancetest.R
import com.example.attendancetest.models.Statu

class StatusAdapter(private val status: List<Statu>) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.heading)
        val footer: TextView = itemView.findViewById(R.id.footer)
        val card: CardView = itemView.findViewById(R.id.cardView)
        val letter: TextView = itemView.findViewById(R.id.letter)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusAdapter.ViewHolder, position: Int) {
        holder.header.text = status[position].status_name.replaceFirstChar { it.uppercase() }
        holder.header.setTextColor(Color.WHITE)
        holder.footer.text = status[position].total
        holder.footer.setTextColor(Color.WHITE)
        holder.letter.text = status[position].status_code.uppercase()
        holder.letter.setTextColor(Color.WHITE)
        holder.card.setCardBackgroundColor(status[position].status_color.toColorInt())
    }

    override fun getItemCount(): Int {
        return status.size
    }
}