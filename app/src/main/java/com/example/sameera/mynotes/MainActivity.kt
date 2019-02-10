package com.example.sameera.mynotes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var listNotes=ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load from DB
        LoadQuery("%")
    }

    private fun LoadQuery(title:String) {
        var dbManager=DB_Manager(this)
        var projections= arrayOf("ID","Title","Description")
        var selectionArgs=arrayOf(title)
        var cursor= dbManager.Query(projections,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if (cursor.moveToFirst()){
            do {
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID,Title,Description))
            }while (cursor.moveToNext())
        }

    }


}
