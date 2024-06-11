package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class GameOptionsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_options)

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val playButton = findViewById<Button>(R.id.playButton)
        val scoresButton = findViewById<Button>(R.id.scoresButton)

        val tipoJuego = intent.getStringExtra("TIPO_JUEGO")

        playButton.setOnClickListener {
            when (tipoJuego) {
                "MEMORIA" -> {
                    val intent = Intent(this, MemoryGameActivity::class.java)
                    startActivity(intent)
                }
                "SUMAS" -> {
                    val intent = Intent(this, SumGameActivity::class.java)
                    startActivity(intent)
                }
                "PALABRAS" -> {
                    val intent = Intent(this, WordGameActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        scoresButton.setOnClickListener {
            val gameType = intent.getStringExtra("TIPO_JUEGO")
            when (gameType) {
                "MEMORIA" -> startActivity(Intent(this, MemoryScoresActivity::class.java))
                "SUMAS" -> startActivity(Intent(this, SumScoresActivity::class.java))
                "PALABRAS" -> startActivity(Intent(this, WordScoresActivity::class.java))
                else -> {
                    Toast.makeText(this, "Tipo de juego desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
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
