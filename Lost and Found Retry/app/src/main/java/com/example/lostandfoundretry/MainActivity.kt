package com.example.lostandfoundretry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//var itemArray = ArrayList<Button>()
var database = FirebaseDatabase.getInstance()
var appListSize = 0L // Keep track the number of items on the app home screen currently
var databaseSize = 0L // Keep track the number of items in the database currently
var updateCount = 5 /* Update count starts at 0 and goes to 5 because there are 6 attirbutes, and everytime
one gets updated, so does the homescreen, but I only want it updated after all 6 attributes are recieved in
the database */
var changeCount = 0
var removed = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.addItem)
        button.setOnClickListener {
            var intent = Intent(this, AddItem::class.java)
            startActivityForResult(intent, 0)
        }

        getSupportActionBar()!!.setTitle("Lost and Found")

        val database = database.reference

        val scrollLayout = findViewById<LinearLayout>(R.id.scrollLayout) // This layout is only the scrollable section

        val thiss = this // So that I can reference this in the database listener

        val databaseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(updateCount == 5) { /* Only once update count has reached 5 will all 6 attributes for the item be
                received and I can update the home screen */

                    databaseSize = dataSnapshot.child("items").childrenCount
                    for (a in appListSize until databaseSize) { /* appListSize is the number of items currently in
                    the app, this will only add the extra items in the database, not the ones already there */

                        val temp = Button(thiss)
                        temp.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        val brand = dataSnapshot.child("items").child(a.toString())
                            .child("brand").value.toString() /* Each item added gets a number, so
                            child(a.toString()) is that number */
                        val item = dataSnapshot.child("items").child(a.toString())
                            .child("item").value.toString()
                        val color = dataSnapshot.child("items").child(a.toString())
                            .child("color").value.toString()
                        val size = dataSnapshot.child("items").child(a.toString())
                            .child("size").value.toString()
                        val date = dataSnapshot.child("items").child(a.toString())
                            .child("date").value.toString()
                        temp.text = brand + ": " + item + " " + date /* The buttons will say the item's brand
                        and type */
                        val temp2 = 1
                        temp.id = temp2

                        temp.setOnClickListener {
                            var intent = Intent(thiss, ClaimItem::class.java)

                            intent.putExtra("brand", brand)
                            intent.putExtra("item", item)
                            intent.putExtra("color", color)
                            intent.putExtra("size", size)
                            intent.putExtra("date", date)
                            intent.putExtra("a", a.toString())

                            startActivityForResult(intent, 1)
                        }

                        val row = LinearLayout(thiss)
                        row.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        row.addView(temp)
                        scrollLayout.addView(row)
                        val layout = findViewById<LinearLayout>(R.id.layout) /* This is the main layout that takes up
                        the home screen */
                        setContentView(layout)
                        appListSize++
                    }
                    updateCount = 0 /* Reset updateCount to 0 so that it won't update again until all 6 attributes of
                    the next item are received */
                } else
                    updateCount++
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(databaseListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == 0) { // Required null check
            if (data.getBooleanExtra("entered", false)) { /* Only if the boolean is true this will
            execute because true means it came back from the AddItem activity and not from ClaimItem activity */
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                val formatted = current.format(formatter)

                val brand = data?.getStringExtra("brand").toString()
                val item = data?.getStringExtra("item").toString()
                val color = data?.getStringExtra("color").toString()
                var size = data?.getStringExtra("size").toString()
                var description = data?.getStringExtra("description").toString()
                val date = formatted

                if (description.isEmpty() || description == "")
                    description = "N/A"
                if (size.isEmpty() || size == "")
                    size = "N/A"

                var database = database.reference
                database.child("items").child((databaseSize).toString()).child("brand").setValue(brand)
                database.child("items").child((databaseSize).toString()).child("item").setValue(item)
                database.child("items").child((databaseSize).toString()).child("color").setValue(color)
                database.child("items").child((databaseSize).toString()).child("size").setValue(size)
                database.child("items").child((databaseSize).toString()).child("description").setValue(description)
                database.child("items").child((databaseSize).toString()).child("date").setValue(date)
            }
        }
    }

    companion object {
        private val TAG = "Lost and Found"
    }
}