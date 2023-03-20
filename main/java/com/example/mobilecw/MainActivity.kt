package com.example.mobilecw

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var newGameButton = findViewById<Button>(R.id.new_game_button)
        newGameButton.setOnClickListener {
            val intent = Intent(this, NewDiceGame::class.java)
            startActivity(intent)}
        var aboutButton = findViewById<Button>(R.id.about_button)
        aboutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.about_popup)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }
}