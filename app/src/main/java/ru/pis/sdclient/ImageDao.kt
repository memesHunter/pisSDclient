package ru.pis.sdclient

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM HistoryImage")
    fun getAll(): List<HistoryImage>

    @Query("SELECT * FROM HistoryImage WHERE uid IN (:imagesIds)")
    fun loadAllByIds(imagesIds: IntArray): List<HistoryImage>

    @Query("SELECT * FROM HistoryImage WHERE filename LIKE :file LIMIT 1")
    fun findByFileName(file: String): HistoryImage

    @Insert
    fun insert(image: HistoryImage)

    @Delete
    fun delete(image: HistoryImage)
}