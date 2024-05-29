package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

class WordGameActivity : AppCompatActivity() {

    private lateinit var hintText: TextView
    private lateinit var scrambledWordText: TextView
    private lateinit var guessEditText: EditText
    private lateinit var checkButton: Button
    private lateinit var chronometer: Chronometer
    private lateinit var word: String
    private lateinit var scrambledWord: String
    private lateinit var hint: String
    private var gameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_game)

        hintText = findViewById(R.id.hintText)
        scrambledWordText = findViewById(R.id.scrambledWordText)
        guessEditText = findViewById(R.id.guessEditText)
        checkButton = findViewById(R.id.checkButton)
        chronometer = findViewById(R.id.chronometer)

        loadWord()
        displayScrambledWord()

        checkButton.setOnClickListener {
            val guess = guessEditText.text.toString()
            if (guess.equals(word, ignoreCase = true)) {
                chronometer.stop()
                val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
                val elapsedSeconds = elapsedMillis / 1000
                showCompletionDialog(elapsedSeconds)
            } else {
                Toast.makeText(this, "Incorrecto. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
            }
        }

        if (!gameStarted) {
            chronometer.start()
            gameStarted = true
        }
    }

    private fun loadWord() {
        val inputStream = resources.openRawResource(R.raw.words)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val words = reader.useLines { it.toList() }
        val wordPair = words[Random.nextInt(words.size)].split(":")
        word = wordPair[0]
        hint = wordPair[1]
    }

    private fun displayScrambledWord() {
        scrambledWord = word.toCharArray().apply { shuffle() }.concatToString()
        scrambledWordText.text = "Palabra: $scrambledWord"
        hintText.text = "Pista: $hint"
    }

    private fun showCompletionDialog(elapsedSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle("¡Enhorabuena!")
            .setMessage("Has encontrado la palabra en $elapsedSeconds segundos")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, GameOptionsActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual
            }
            .show()
    }
}
