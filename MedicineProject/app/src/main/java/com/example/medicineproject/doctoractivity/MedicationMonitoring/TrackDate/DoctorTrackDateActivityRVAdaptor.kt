package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackDate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvDateBinding

class DoctorTrackDateActivityRVAdaptor(private val patientDate: List<String>) : RecyclerView.Adapter<DoctorTrackDateActivityRVAdaptor.ViewHolder>() {
    private var mListener: OnItemClickListener? = null // Make the listener nullable

    //interface to handle item click
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // Set the listener for item click
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding: RvDateBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Set the click listener here if it's not null
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView, mListener) // Pass mListener here
    }

    override fun getItemCount(): Int {
        return patientDate.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = patientDate[position]
        holder.apply {
            binding.apply {
                tvDate.text ="Date: $currentItem"
            }
        }
    }
}