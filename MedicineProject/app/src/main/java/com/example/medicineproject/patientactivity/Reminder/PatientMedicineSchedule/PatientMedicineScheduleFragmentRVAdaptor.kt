package com.example.medicineproject.patientactivity.Reminder.PatientMedicineSchedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvPatientmedicinehistoryBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail

class PatientMedicineScheduleFragmentRVAdaptor(private val MedicineList: ArrayList<UserMedicineDetail>): RecyclerView.Adapter<PatientMedicineScheduleFragmentRVAdaptor.ViewHolder>() {
    private var mListener: OnItemClickListener? = null // Make the listener nullable

    // Interface for click event handling
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // Function to set the click listener for the adapter
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding: RvPatientmedicinehistoryBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Set the click listener here if it's not null
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvPatientmedicinehistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView, mListener) // Return a new ViewHolder with the inflated view and listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = MedicineList[position]
        currentItem.let {
            holder.binding.apply {
                tvMedicinePatientHistory.text = "Medicine: ${it.medicineName}"
                tvTimePatientHistory.text = "Time: ${it.hours} Hours"
                tvPillPatientHistory.text = "${it.pills} pills"
                tvDatePillHistory.text="Date:${it.date}"
                tvTotalPillPatientHistory.text="Total Pill:${it.totalpills}"
                tvPillsLeftPatientPatientHistory.text="Pills Left: ${it.pillsleft}"

            }
        }
    }

    override fun getItemCount(): Int {
        return MedicineList.size
    }




}