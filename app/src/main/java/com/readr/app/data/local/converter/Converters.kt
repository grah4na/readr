package com.readr.app.data.local.converter

import androidx.room.TypeConverter
import com.readr.app.data.model.EntryType

class Converters {
    @TypeConverter
    fun fromEntryType(value: EntryType): String = value.name

    @TypeConverter
    fun toEntryType(value: String): EntryType = EntryType.valueOf(value)
}