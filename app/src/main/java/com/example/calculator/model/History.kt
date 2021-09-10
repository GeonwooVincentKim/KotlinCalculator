package com.example.calculator.model

import androidx.room.Entity

@Entity
data class History(
    val uniqueID: Int?,
    val expression: String?,
    val result: String
)
