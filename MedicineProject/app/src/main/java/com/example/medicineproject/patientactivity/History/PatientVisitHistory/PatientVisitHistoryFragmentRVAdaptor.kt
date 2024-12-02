package com.example.medicineproject.patientactivity.History.PatientVisitHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvDateBinding

class PatientVisitHistoryFragmentRVAdaptor(private val patientDate: MutableList<String>) : RecyclerView.Adapter<PatientVisitHistoryFragmentRVAdaptor.ViewHolder>() {

    private var mListener: OnItemClickListener? = null // Make the listener nullable

    // Interface to handle item click events
    interface OnItemClickListener { //called when item click
        fun onItemClick(position: Int)
    }

    // Function to set the listener for item clicks
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding: RvDateBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
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
                tvDate.text = currentItem
            }
        }
    }
}