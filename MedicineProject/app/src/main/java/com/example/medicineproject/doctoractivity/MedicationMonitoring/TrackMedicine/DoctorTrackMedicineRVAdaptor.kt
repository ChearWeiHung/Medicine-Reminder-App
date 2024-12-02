package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackMedicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvMedicationlistPatientBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail


class DoctorTrackMedicineRVAdaptor(private val MedicineList: List<UserMedicineDetail>): RecyclerView.Adapter<DoctorTrackMedicineRVAdaptor.ViewHolder>() {
    private var mListener: OnItemClickListener? = null // Make the listener nullable

    //interface to handle item click
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    // Set the listener for item click
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding: RvMedicationlistPatientBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Set the click listener for the item view
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition) // Call the listener when an item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvMedicationlistPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView, mListener) // Pass mListener here
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = MedicineList[position]
        currentItem.let {
            holder.binding.apply {
                tvMedicineNamePatient.text = "Medicine: ${it.medicineName}"
                tvMedicineTimePatient.text = "Time: ${it.hours} Hours"
                tvPillAmountPatient.text = "${it.pills} pills"
                tvTotalPillPatient.text="Total Pill:${it.totalpills}"
                tvPillsLeftPatient.text="Pills left:${it.pillsleft}"
            }
        }
    }

    override fun getItemCount(): Int {
        return MedicineList.size
    }
}