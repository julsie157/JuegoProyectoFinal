package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SumGameActivity : AppCompatActivity() {

    private lateinit var targetNumberText: TextView
    private lateinit var numberGrid: GridLayout
    private lateinit var chronometer: Chronometer
    private var targetNumber: Int = 0
    private var numbers: MutableList<Int> = mutableListOf()
    private var gameStarted = false
    private var currentSum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sum_game)

        targetNumberText = findViewById(R.id.targetNumber)
        numberGrid = findViewById(R.id.numberGrid)
        chronometer = findViewById(R.id.chronometer)

        generateGame()

        // Iniciar el cronómetro cuando el juego comience
        chronometer.start()
        gameStarted = true
    }

    private fun generateGame() {
        // Generar un número objetivo aleatorio entre 200 y 500
        targetNumber = Random.nextInt(200, 501)
        targetNumberText.text = "Objetivo: $targetNumber"

        // Generar números aleatorios que se sumarán al número objetivo
        numbers.clear()
        numberGrid.removeAllViews()
        val operationsCount = 6  // Siempre mostrar 6 números

        for (i in 0 until operationsCount) {
            val number = Random.nextInt(1, 100)
            numbers.add(number)
        }

        numbers.forEach { number ->
            val button = Button(this).apply {
                text = number.toString()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 8, 8) // Espacio entre los números
                }
                setOnClickListener { onNumberClicked(number) }
            }
            numberGrid.addView(button)
        }

        currentSum = 0
    }

    private fun onNumberClicked(number: Int) {
        currentSum += number
        if (currentSum == targetNumber) {
            chronometer.stop()
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val elapsedSeconds = elapsedMillis / 1000
            showCompletionDialog(elapsedSeconds)
        } else if (currentSum > targetNumber) {
            // El jugador se ha pasado del objetivo, reseteamos el juego
            chronometer.stop()
            showLossDialog()
            finish()
        } else {
            Toast.makeText(this, "Suma actual: $currentSum", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCompletionDialog(elapsedSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle("¡Enhorabuena!")
            .setMessage("Has alcanzado el objetivo en $elapsedSeconds segundos")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, GameOptionsActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual
            }
            .show()
    }

    private fun showLossDialog() {
        AlertDialog.Builder(this)
            .setTitle("¡Has perdido!")
            .setMessage("Te has pasado del objetivo.")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, GameOptionsActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual
            }
            .show()
    }
}
