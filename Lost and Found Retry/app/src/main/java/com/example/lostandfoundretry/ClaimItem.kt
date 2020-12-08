package com.example.lostandfoundretry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ClaimItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.claim_item)

        getSupportActionBar()!!.setTitle("Lost and Found")

        val attributes = findViewById<TextView>(R.id.attributes)

        val colorText = findViewById<EditText>(R.id.color)
        val sizeText = findViewById<EditText>(R.id.size)

        val button = findViewById<Button>(R.id.enterAttributes)
        val instructions = findViewById<TextView>(R.id.instructions)
        val goBack = findViewById<Button>(R.id.goBack)

        attributes.text = "\n" + intent.getStringExtra("brand") + " " + intent.getStringExtra("item") +
                "\n" + "\nFound on: " + intent.getStringExtra("date")

        if(intent.getStringExtra("size") == "N/A") { /* If there was no size entered, then the user won't have
        to enter a size */
            sizeText.hint = "N/A" // The text box will say N/A by default
            sizeText.setEnabled(false) // The text box for size is not uneditable
            button.setOnClickListener {
                if(colorText.text.toString().equals(intent.getStringExtra("color"))) {
                    attributes.text = "\n" + intent.getStringExtra("brand") + " " + intent.getStringExtra("item") +
                            "\n" + "\nFound on: " + intent.getStringExtra("date") +
                            "\n\nCome to 2117 Mitchell Building to retrieve your lost item"
                }
                else {
                    attributes.text = "\n" + intent.getStringExtra("brand") + " " + intent.getStringExtra("item") +
                            "\n" + "\nFound on: " + intent.getStringExtra("date") +
                            "\n\nSorry, you input the wrong information, if this is your item however, come to 2117 Mitchell Building"
                }

                colorText.visibility = View.INVISIBLE
                sizeText.visibility = View.INVISIBLE
                instructions.visibility = View.INVISIBLE
                button.visibility = View.INVISIBLE
                goBack.visibility = View.VISIBLE
            }
        }
        else {
            button.setOnClickListener {
                if(colorText.text.toString().equals(intent.getStringExtra("color")) &&
                    sizeText.text.toString().equals(intent.getStringExtra("size"))) { /* If the user gets both
                    the size and the color of the item right, then they will be able to claim their item */
                    attributes.text = "\n" + intent.getStringExtra("brand") + " " + intent.getStringExtra("item") +
                            "\n" + "\nFound on: " + intent.getStringExtra("date") +
                            "\n\nCome to 2117 Mitchell Building to retrieve your lost item"
                }
                else {
                    attributes.text = "\n" + intent.getStringExtra("brand") + " " + intent.getStringExtra("item") +
                            "\n" + "\nFound on: " + intent.getStringExtra("date") +
                            "\n\nSorry, you input the wrong information, if this is your item however, come to 2117 Mitchell Building"
                }

                colorText.visibility = View.INVISIBLE
                sizeText.visibility = View.INVISIBLE
                instructions.visibility = View.INVISIBLE
                button.visibility = View.INVISIBLE
                goBack.visibility = View.VISIBLE
                // Hiding all of the layout items that aren't necessary anymore
            }
        }



        goBack.setOnClickListener {

            val resultIntent = Intent()
            resultIntent.putExtra("brand", intent.getStringExtra("brand"))
            resultIntent.putExtra("item", intent.getStringExtra("item"))
            resultIntent.putExtra("color", intent.getStringExtra("color"))
            resultIntent.putExtra("size", intent.getStringExtra("size"))
            resultIntent.putExtra("description", intent.getStringExtra("description"))
            resultIntent.putExtra("a", intent.getStringExtra("a"))
            resultIntent.putExtra("correct", true)
            setResult(1, resultIntent)
            finish()
        }
    }

    companion object {
        private val TAG = "Lost and Found"
    }
}