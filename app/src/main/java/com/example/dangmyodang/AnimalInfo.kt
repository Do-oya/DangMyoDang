package com.example.dangmyodang

data class AnimalInfo (
    val animal: List<Animal>
) {data class Animal(
    val name: String,
    val facilityType: String,
    val tell: String,
    val time: String,
    val holly: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
}