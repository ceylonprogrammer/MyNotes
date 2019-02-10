package com.example.sameera.mynotes

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {


    val dbTable = "Notes"
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        try {
            val bundle: Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                titleEditText.setText(bundle.getString("name"))
                descEditText.setText(bundle.getString("desc"))
            }
        } catch (ex: Exception) {

        }
    }

    fun addFunc(view: View) {
        var dbManager = DB_Manager(this)
        var values = ContentValues()
        values.put("Title", titleEditText.text.toString())
        values.put("Description", descEditText.text.toString())
        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error Adding Note", Toast.LENGTH_SHORT).show()
            }

        } else {
            var selectionArgs = arrayOf(id.toString())
            var ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error Adding Note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
