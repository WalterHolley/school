package com.umsl.wdhq58.prj2.utility.api.data

data class PopularPersons(
    val results: List<PopularPerson>
)

data class PopularPerson(
    val id:Int,
    val birthday:String,
    val known_for_department:String,
    val biography:String,
    val deathday:String,
    val name:String,
    val also_known_as:List<String>,
    val popularity:Double,
    val profile_path:String,
    val place_of_birth: String
)