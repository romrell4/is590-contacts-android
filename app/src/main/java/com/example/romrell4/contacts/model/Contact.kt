package com.example.romrell4.contacts.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by romrell4 on 2/2/18
 */
data class Contact(
        var name: CharSequence,
        var imageRes: Int?,
        var company: CharSequence?,
        var position: CharSequence?,
        var email: CharSequence?,
        var cellPhone: CharSequence?,
        var spouseName: CharSequence?,
        var address: CharSequence?,
        var bio: CharSequence?,
        var bitmap: Bitmap? = null
): Parcelable {
    constructor(parcel: Parcel): this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Bitmap::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name.toString())
        parcel.writeValue(imageRes)
        parcel.writeString(company?.toString())
        parcel.writeString(position?.toString())
        parcel.writeString(email?.toString())
        parcel.writeString(cellPhone?.toString())
        parcel.writeString(spouseName?.toString())
        parcel.writeString(address?.toString())
        parcel.writeString(bio?.toString())
        parcel.writeParcelable(bitmap, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }

}