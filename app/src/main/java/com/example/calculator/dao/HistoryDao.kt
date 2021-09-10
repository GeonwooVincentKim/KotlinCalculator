package com.example.calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calculator.model.History

@Dao
interface HistoryDao {
    /* Write Query */
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Delete
    fun delete(history: History)

    /* Deprecated Codes -> Unnecessary codes here */
    @Query("SELECT * FROM history WHERE RESULT LIKE :result")
    fun findByResult(result: String): List<History>

    /* Deprecated Codes -> Unnecessary codes here*/
    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    fun findByResultOne(result: String): History
}
