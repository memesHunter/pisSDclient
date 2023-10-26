package ru.pis.sdclient

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryImage(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "filename") val filename: String?,
    @ColumnInfo(name = "prompt") val prompt: String,
    @ColumnInfo(name = "negative") val negative: String,
    @ColumnInfo(name = "steps") val steps: Int,
    @ColumnInfo(name = "sampler") val sampler: String,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "height") val height: Int
)