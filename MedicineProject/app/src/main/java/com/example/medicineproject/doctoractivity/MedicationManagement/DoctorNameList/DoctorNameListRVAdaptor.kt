package com.example.medicineproject.doctoractivity.MedicationManagement.DoctorNameList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvPatientlistBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserAccount

class DoctorNameListRVAdaptor(
    private var patientList: ArrayList<UserAccount> // Mutable list of patients
) : RecyclerView.Adapter<DoctorNameListRVAdaptor.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    private var fullPatientList = ArrayList(patientList)
    var filteredPatientList = ArrayList(patientList)  // This will be filtered list

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding: RvPatientlistBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvPatientlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return filteredPatientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredPatientList[position]
        holder.binding.tvPatientName.text = currentItem.name
    }

    // Filter function to update the filtered list
    fun filter(query: String) {
        filteredPatientList.clear()
        if (query.isEmpty()) {
            filteredPatientList.addAll(patientList) // Show all if no query
        } else {
            patientList.filter {
                it.name?.contains(query, ignoreCase = true) == true
            }.let {
                filteredPatientList.addAll(it)
            }
        }
        notifyDataSetChanged()
    }

    // Update the full list when new data is fetched
    fun updateFullList(newList: List<UserAccount>) {
        patientList.clear()  // Update the original list
        patientList.addAll(newList)

        filteredPatientList.clear() // Reset filtered list
        filteredPatientList.addAll(newList) // Add all data to filtered list initially

        fullPatientList.clear() // Keep full list to maintain original data
        fullPatientList.addAll(newList)

        notifyDataSetChanged()  // Notify adapter that data has changed
    }

}
