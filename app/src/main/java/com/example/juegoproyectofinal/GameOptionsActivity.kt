package com.example.juegoproyectofinal
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_options)

        val playButton = findViewById<Button>(R.id.playButton)
        val scoresButton = findViewById<Button>(R.id.scoresButton)

        playButton.setOnClickListener {
            // Navegar a la actividad del juego correspondiente
            val gameType = intent.getStringExtra("GAME_TYPE")
            when (gameType) {
                "MEMORY" -> startActivity(Intent(this, MemoryGameActivity::class.java))
                "SUM" -> startActivity(Intent(this, SumGameActivity::class.java))
                "WORD" -> startActivity(Intent(this, WordGameActivity::class.java))
                else -> {
                    // Manejar el caso de que no se reconozca el tipo de juego
                    Toast.makeText(this, "Tipo de juego desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        scoresButton.setOnClickListener {
            // Navegar a la actividad de puntuaciones correspondiente
            val gameType = intent.getStringExtra("GAME_TYPE")
            when (gameType) {
                "MEMORY" -> startActivity(Intent(this, MemoryScoresActivity::class.java))
                "SUM" -> startActivity(Intent(this, SumScoresActivity::class.java))
                "WORD" -> startActivity(Intent(this, WordScoresActivity::class.java))
                else -> {
                    // Manejar el caso de que no se reconozca el tipo de juego
                    Toast.makeText(this, "Tipo de juego desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
