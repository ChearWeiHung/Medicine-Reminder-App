package com.example.medicineproject.doctoractivity.MedicationManagement

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.R
import com.example.medicineproject.databinding.RvPatientmedicinelistBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail

class DoctorAddMedicationRVAdaptor : RecyclerView.Adapter<DoctorAddMedicationRVAdaptor.ViewHolder>() {

    private var patientMedicineList = ArrayList<UserMedicineDetail>()
    private var selectedPosition = RecyclerView.NO_POSITION // //set no position at start

    // LiveData observer to update the adapter's data
    fun updateList(newList: ArrayList<UserMedicineDetail>) {
        patientMedicineList = newList
        notifyDataSetChanged() // Notify the adapter to update the list
    }

    inner class ViewHolder(val binding: RvPatientmedicinelistBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                updateSelection(adapterPosition) // Update selection when item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvPatientmedicinelistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = patientMedicineList[position]
        holder.binding.apply {
            tvMedicineName.text = "Medicine: ${currentItem.medicineName}"
            tvMedicineTime.text = "Time: ${currentItem.hours} Hours"
            tvPillAmount.text = "${currentItem.pills} pills"
            tvTotalPill.text = "Total Pill: ${currentItem.totalpills}"

            // Change background color based on selection
            root.setBackgroundColor(
                if (position == selectedPosition) {
                    root.context.getColor(R.color.selected_item_background)
                } else {
                    root.context.getColor(R.color.default_item_background)
                }
            )
        }
    }

    override fun getItemCount(): Int = patientMedicineList.size

    private fun updateSelection(position: Int) {
        val previousPosition = selectedPosition // Store the previously selected position
        selectedPosition = position // Update the selected position to the clicked item

        // Notify the adapter to refresh the previously and newly selected items
        notifyItemChanged(previousPosition) // to notify rebind again previous position (set back to default color based on the logic
        notifyItemChanged(selectedPosition)

        // Debug log to check if selection is captured
        Log.d("AdapterSelection", "Updated position: $position")
    }

    //get the selected item
    fun getSelectedItem(): UserMedicineDetail? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            Log.d("AdapterSelection", "Selected item at position: $selectedPosition") // Debug log
            patientMedicineList[selectedPosition]
        } else {
            Log.d("AdapterSelection", "No item selected") // Debug log
            null
        }
    }

}
