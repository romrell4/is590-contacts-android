package com.example.romrell4.contacts.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by romrell4 on 2/2/18
 */
data class Contact(
        var name: String,
        var imageRes: Int?,
        var company: String?,
        var position: String?,
        var email: String?,
        var cellPhone: String?,
        var spouseName: String?,
        var address: String?,
        var bio: String?
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
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(imageRes)
        parcel.writeString(company)
        parcel.writeString(position)
        parcel.writeString(email)
        parcel.writeString(cellPhone)
        parcel.writeString(spouseName)
        parcel.writeString(address)
        parcel.writeString(bio)
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