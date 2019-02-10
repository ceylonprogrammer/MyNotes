package com.example.sameera.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DB_Manager {
    //Database Name
    var dbName="Mynotes"
    //Table Name
    var dbTable="Notes"
    //Collum Name
    var colID="ID"
    var colTitle="Title"
    var colDesc="Description"
    //Database Version
    var dbVersion=1
    //Create table if not exist in database(ID INTEGER PRIMERY_KEY,TITLE:TEXT,DESCRIPTION:TEXT)
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colID + " INTEGER PRIMARY KEY, " + colTitle + " TEXT, " + colDesc + " TEXT);"
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
        var db=DataBaseHelperNotes(context)
        sqlDB=db.writableDatabase
    }
    inner class DataBaseHelperNotes:SQLiteOpenHelper{


        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion)
        {
            this.context=context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"DataBase Created",Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
db!!.execSQL("DROP TABLE IF EXISTS"+dbTable)
        }


    }
    fun insert(values:ContentValues):Long{
        val ID=sqlDB!!.insert(dbTable,"",values)
        return ID

    }
    fun Query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{

        val qb = SQLiteQueryBuilder()
        qb.tables=dbTable
        var cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
        return cursor
    }
    fun delete(selection:String,selectionArgs: Array<String>):Int{
val count=sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int{
val count=sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }
}