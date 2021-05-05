package com.example.newsnow.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSource(source: com.example.newsnow.model.Source):String {

        return source.name
    }


    @TypeConverter
    fun toSource(name:String):com.example.newsnow.model.Source {

        return  com.example.newsnow.model.Source(name,name)
    }

}