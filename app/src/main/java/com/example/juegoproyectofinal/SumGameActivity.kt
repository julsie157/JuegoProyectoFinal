package com.example.juegoproyectofinal

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class SumGameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
                textSize = 24f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(resources.getColor(R.color.white))
                setBackgroundColor(resources.getColor(R.color.purple_200))
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
        } else {
            Toast.makeText(this, "Suma actual: $currentSum", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCompletionDialog(elapsedSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle("¡Enhorabuena!")
            .setMessage("Has alcanzado el objetivo en $elapsedSeconds segundos")
            .setPositiveButton("OK") { _, _ ->
                // Guardar la puntuación en Firebase
                saveScore(elapsedSeconds, "SUM")
                // Ir a la GameOptionsActivity
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
                // Ir a la GameOptionsActivity
                val intent = Intent(this, GameOptionsActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual
            }
            .show()
    }

    private fun saveScore(time: Long, gameType: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            val username = email?.substringBefore("@")
            val database = FirebaseDatabase.getInstance()
            val scoresRef = database.getReference("scores").child(gameType)
            val newScoreRef = scoresRef.push()
            val score = Score(username ?: "unknown", time)
            newScoreRef.setValue(score).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Puntuación guardada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al guardar la puntuación", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Debe iniciar sesión para guardar la puntuación", Toast.LENGTH_SHORT).show()
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
