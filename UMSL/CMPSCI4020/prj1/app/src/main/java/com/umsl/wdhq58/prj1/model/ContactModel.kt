package com.umsl.wdhq58.prj1.model


data class ContactModel(val lastName: String = "", val firstName: String = "", val address: String, val phone: String): java.io.Serializable {

    fun getFullName():String {
        return "$firstName $lastName"
    }
}