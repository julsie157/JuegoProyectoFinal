package com.example.juegoproyectofinal

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.juegoproyectofinal.models.Score
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MemoryGameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var gridLayout: GridLayout
    private lateinit var cardButtons: Array<Button>
    private var cardImages: IntArray = intArrayOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
        R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8,
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
        R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8
    )
    private var cardsFlipped = BooleanArray(16)
    private var firstCardIndex: Int? = null
    private var secondCardIndex: Int? = null
    private var matchCount = 0
    private var failCount = 0
    private lateinit var failCountText: TextView
    private lateinit var chronometer: Chronometer
    private var gameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_game)

        auth = FirebaseAuth.getInstance()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        gridLayout = findViewById(R.id.gridLayout)
        failCountText = findViewById(R.id.failCountText)
        chronometer = findViewById(R.id.chronometer)
        cardButtons = Array(16) { Button(this) }
        cardImages.shuffle()

        for (i in 0..15) {
            val button = cardButtons[i]
            button.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8) // Espacio entre las cartas
            }
            button.setBackgroundResource(R.drawable.card_back)
            button.setOnClickListener { onCardClicked(i) }
            gridLayout.addView(button)
        }
    }

    private fun onCardClicked(index: Int) {
        if (!gameStarted) {
            chronometer.start()
            gameStarted = true
        }

        if (cardsFlipped[index] || (firstCardIndex != null && secondCardIndex != null)) {
            return
        }

        cardButtons[index].setBackgroundResource(cardImages[index])
        cardsFlipped[index] = true

        if (firstCardIndex == null) {
            firstCardIndex = index
        } else {
            secondCardIndex = index
            checkForMatch()
        }
    }

    private fun checkForMatch() {
        val firstIndex = firstCardIndex ?: return
        val secondIndex = secondCardIndex ?: return

        if (cardImages[firstIndex] == cardImages[secondIndex]) {
            matchCount++
            if (matchCount == 8) {
                chronometer.stop()
                val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
                val elapsedSeconds = elapsedMillis / 1000
                showCompletionDialog(elapsedSeconds)
            }
            firstCardIndex = null
            secondCardIndex = null
        } else {
            failCount++
            failCountText.text = "Fallos: $failCount"
            Handler().postDelayed({
                cardButtons[firstIndex].setBackgroundResource(R.drawable.card_back)
                cardButtons[secondIndex].setBackgroundResource(R.drawable.card_back)
                cardsFlipped[firstIndex] = false
                cardsFlipped[secondIndex] = false
                firstCardIndex = null
                secondCardIndex = null

                if (failCount >= 12) {
                    chronometer.stop()
                    showLossDialog()
                }
            }, 1000)
        }
    }

    private fun showCompletionDialog(elapsedSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle("¡Enhorabuena!")
            .setMessage("Has encontrado todas las parejas en $elapsedSeconds segundos")
            .setPositiveButton("OK") { _, _ ->
                saveScore(elapsedSeconds, "MEMORY")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }


    private fun showLossDialog() {
        AlertDialog.Builder(this)
            .setTitle("¡Has perdido!")
            .setMessage("Te has pasado del máximo de 12 fallos.")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
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
