package com.inex.expensetracker.model

import android.os.Parcel
import android.os.Parcelable

class SelectionModel(var id: Int, var name: String? = null , var isActive: Boolean = false):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeByte(if (isActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectionModel> {
        override fun createFromParcel(parcel: Parcel): SelectionModel {
            return SelectionModel(parcel)
        }

        override fun newArray(size: Int): Array<SelectionModel?> {
            return arrayOfNulls(size)
        }
    }
}