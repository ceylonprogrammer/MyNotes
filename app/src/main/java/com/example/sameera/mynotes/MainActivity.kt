package com.example.sameera.mynotes

import android.app.AlertDialog
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    //Shared preferences
    var mSharedPreferances: SharedPreferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSharedPreferances = this.getSharedPreferences("My_Data", android.content.Context.MODE_PRIVATE)

        //Load newest first soring as Default settings to sort
        //Always show newest first when app is open
        var mSorting = mSharedPreferances!!.getString("Sort", "newest")
        when (mSorting) {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }

        //Load from DB
//        loadQueryAscending("%")
    }


    override fun onResume() {
        super.onResume()
//        loadQueryAscending("%")
        //Load newest first soring as Default settings to sort
        //Always show newest first when app is open
        var mSorting = mSharedPreferances!!.getString("Sort", "newest")
        when (mSorting) {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }
    }

    private fun loadQueryAscending(title: String) {
        var dbManager = DB_Manager(this)
        var projections = arrayOf("ID", "Title", "Description")
        var selectionArgs = arrayOf(title)
        //Sort by title

        var cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        //Ascending sorting

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }
        //Adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)

        //Set Adapter
        notesListView.adapter = myNotesAdapter

        //Get total number of task from the ListView
        val total = notesListView.count

        //Action Bar
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total of Note(s) in the list"
        }
    }

    private fun loadQueryDescending(title: String) {
        var dbManager = DB_Manager(this)
        var projections = arrayOf("ID", "Title", "Description")
        var selectionArgs = arrayOf(title)
        //Sort by title

        var cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        //Descending sorting

        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToPrevious())
        }
        //Adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)

        //Set Adapter
        notesListView.adapter = myNotesAdapter

        //Get total number of task from the ListView
        val total = notesListView.count

        //Action Bar
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total of Note(s) in the list"
        }
    }

    private fun loadQueryNewest(title: String) {
        var dbManager = DB_Manager(this)
        var projections = arrayOf("ID", "Title", "Description")
        var selectionArgs = arrayOf(title)
        //Sort by ID

        var cursor = dbManager.Query(projections, "ID like ?", selectionArgs, "ID")
        listNotes.clear()
        //Newest First Sorting

        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToPrevious())
        }
        //Adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)

        //Set Adapter
        notesListView.adapter = myNotesAdapter

        //Get total number of task from the ListView
        val total = notesListView.count

        //Action Bar
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total of Note(s) in the list"
        }
    }

    private fun loadQueryOldest(title: String) {
        var dbManager = DB_Manager(this)
        var projections = arrayOf("ID", "Title", "Description")
        var selectionArgs = arrayOf(title)
        //Sort by ID

        var cursor = dbManager.Query(projections, "ID like ?", selectionArgs, "ID")
        listNotes.clear()
        //Oldest First Sorting .Show oldest records first .Lesser Big ID shows first

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }
        //Adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)

        //Set Adapter
        notesListView.adapter = myNotesAdapter

        //Get total number of task from the ListView
        val total = notesListView.count

        //Action Bar
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total of Note(s) in the list"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //search view
        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQueryAscending("" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQueryAscending("" + newText + "%")
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    startActivity(Intent(this, AddNoteActivity::class.java))
                }
                R.id.action_sort -> {
                    // Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    //Show sorting dialog
                    showSortDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        //List of sorting option available
        val sortOptions = arrayOf("Newest", "Oldest", "Title(Ascending)", "Title(Descending)")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Sort by")
        mBuilder.setIcon(R.drawable.ic_action_sort)
        mBuilder.setSingleChoiceItems(sortOptions, -1) { dialogInterface, i ->
            if (i == 0) {
                //Show Newest First
                Toast.makeText(this, "Newest", Toast.LENGTH_SHORT).show()
                val editor = mSharedPreferances!!.edit()
                editor.putString("Sort", "newest") //Where 'Sort' is key and 'newest' is value
                editor.apply()//Apply and save the value in our shared Preferences
                loadQueryNewest("%")
            }
            if (i == 1) {
                //Show Oldest First
                Toast.makeText(this, "Oldest", Toast.LENGTH_SHORT).show()
                val editor = mSharedPreferances!!.edit()
                editor.putString("Sort", "oldest") //Where 'Sort' is key and 'oldest' is value
                editor.apply()//Apply and save the value in our shared Preferences
                loadQueryOldest("%")

            }
            if (i == 2) {
                //Title Ascending
                Toast.makeText(this, "Title(Ascending)", Toast.LENGTH_SHORT).show()
                val editor = mSharedPreferances!!.edit()
                editor.putString("Sort", "ascending") //Where 'Sort' is key and 'ascending' is value
                editor.apply()//Apply and save the value in our shared Preferences
                loadQueryAscending("%")

            }
            if (i == 3) {
                //Title Descending
                Toast.makeText(this, "Title(Descending)", Toast.LENGTH_SHORT).show()
                val editor = mSharedPreferances!!.edit()
                editor.putString("Sort", "descending") //Where 'Sort' is key and 'descending' is value
                editor.apply()//Apply and save the value in our shared Preferences
                loadQueryDescending("%")

            }
            dialogInterface.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNoteAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNoteAdapter: ArrayList<Note>) : super() {
            this.listNoteAdapter = listNoteAdapter
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //Inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            var myNote = listNoteAdapter[position]
            myView.titleTextView.text = myNote.nodeName
            myView.descriptionTextView.text = myNote.nodeDesc
            //delete button click
            myView.deleteBtn.setOnClickListener {
                var dbManager = DB_Manager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQueryAscending("%")
            }
            //Edit and Update Button clicked
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            //Copy button clicked
            myView.copyBtn.setOnClickListener {
                //get title
                var title = myView.titleTextView.text.toString()
                //get description
                var description = myView.descriptionTextView.text.toString()
                //concatinate title and description
                val s = title + "\n" + description
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s //add to clipboard
                Toast.makeText(this@MainActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            //share button clicked
            myView.shareBtn.setOnClickListener {
                //get title
                var title = myView.titleTextView.text.toString()
                //get description
                var description = myView.descriptionTextView.text.toString()
                //concatinate title and description
                val s = title + "\n" + description
                //share using intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))

            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNoteAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNoteAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: Note) {
        var intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeID) //put id
        intent.putExtra("name", myNote.nodeName) //put name
        intent.putExtra("desc", myNote.nodeDesc) //put description
        startActivity(intent) //start activity

    }
}


