package com.example.juegoproyectofinal
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val memoryGameButton = findViewById<Button>(R.id.memoryGameButton)
        val calculationGameButton = findViewById<Button>(R.id.calculationGameButton)
        val wordGameButton = findViewById<Button>(R.id.wordsGameButton)

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
}

