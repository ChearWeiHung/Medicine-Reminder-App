package com.example.medicineproject.patientactivity.History

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.R
import com.example.medicineproject.databinding.RvPatientmedicinehistoryBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail

class PatientMedicationDetailActivityRVAdaptor(
    private var medicineList: ArrayList<UserMedicineDetail>
) : RecyclerView.Adapter<PatientMedicationDetailActivityRVAdaptor.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION // default=no selected

    inner class ViewHolder(val binding: RvPatientmedicinehistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {  // Set up a click listener for the item
                updateSelection(adapterPosition) // Update selection is called when item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvPatientmedicinehistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = medicineList[position]
        holder.binding.apply {
            tvMedicinePatientHistory.text = "Medicine: ${currentItem.medicineName}"
            tvTimePatientHistory.text = "Time: ${currentItem.hours} Hours"
            tvPillPatientHistory.text = "${currentItem.pills} pills"
            tvDatePillHistory.text = "Date: ${currentItem.date}"
            tvTotalPillPatientHistory.text = "Total Pill: ${currentItem.totalpills}"
            tvPillsLeftPatientPatientHistory.text = "Pills Left: ${currentItem.pillsleft}"

            // Change background color based on selection
            root.setBackgroundColor(
                if (position == selectedPosition) {
                    // Selected item color
                    root.context.getColor(R.color.selected_item_background)
                } else {
                    // Default item color
                    root.context.getColor(R.color.default_item_background)
                }
            )
        }
    }

    override fun getItemCount(): Int = medicineList.size

    // update the selection and notify adapter
    private fun updateSelection(position: Int) {
        val previousPosition = selectedPosition // Save the previously selected position
        selectedPosition = position // Update to the new selected position
        notifyItemChanged(previousPosition) // Notify adapter to refresh the old selected item
        notifyItemChanged(selectedPosition) // Notify adapter to refresh the new selected item
    }

    // Get the selected item
    fun getSelectedItem(): UserMedicineDetail? { //return the selected item
        return if (selectedPosition != RecyclerView.NO_POSITION) medicineList[selectedPosition] else null
    }

    // Function to update the list of medicines in the adapter
    fun updateList(newList: List<UserMedicineDetail>) {
        medicineList.clear()  // Clear the existing list
        medicineList.addAll(newList)  // Add the new list
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }
}
