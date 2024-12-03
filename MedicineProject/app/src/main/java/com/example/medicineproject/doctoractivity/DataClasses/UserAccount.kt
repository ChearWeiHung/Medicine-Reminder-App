package com.example.medicineproject.doctoractivity.DataClasses

import android.os.Parcel
import android.os.Parcelable

data class UserAccount(val name: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun describeContents(): Int {
        return 0 //
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name) // Write the name property to the Parcel
    }

    companion object CREATOR : Parcelable.Creator<UserAccount> {
        override fun createFromParcel(parcel: Parcel): UserAccount {
            return UserAccount(parcel)
        }

        override fun newArray(size: Int): Array<UserAccount?> {
            return arrayOfNulls(size)
        }
    }
}
