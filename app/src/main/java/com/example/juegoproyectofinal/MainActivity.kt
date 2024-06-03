package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val memoryGameButton = findViewById<Button>(R.id.memoryGameButton)
        val calculationGameButton = findViewById<Button>(R.id.calculationGameButton)
        val wordGameButton = findViewById<Button>(R.id.wordGameButton)

        memoryGameButton.setOnClickListener {
            val intent = Intent(this, GameOptionsActivity::class.java)
            intent.putExtra("GAME_TYPE", "MEMORY")
            startActivity(intent)
        }

        calculationGameButton.setOnClickListener {
            val intent = Intent(this, GameOptionsActivity::class.java)
            intent.putExtra("GAME_TYPE", "SUM")
            startActivity(intent)
        }

        wordGameButton.setOnClickListener {
            val intent = Intent(this, GameOptionsActivity::class.java)
            intent.putExtra("GAME_TYPE", "WORD")
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

