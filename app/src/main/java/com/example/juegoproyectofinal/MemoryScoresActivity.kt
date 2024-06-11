package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juegoproyectofinal.adapters.ScoresAdapter
import com.example.juegoproyectofinal.models.Score
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MemoryScoresActivity : AppCompatActivity() {

    private lateinit var scoresList: RecyclerView
    private lateinit var scoresAdapter: ScoresAdapter
    private lateinit var scores: MutableList<Score>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        scoresList = findViewById(R.id.scoresList)
        scores = mutableListOf()
        scoresAdapter = ScoresAdapter(scores)
        scoresList.adapter = scoresAdapter
        scoresList.layoutManager = LinearLayoutManager(this)

        fetchScores()
    }

    private fun fetchScores() {
        val database = FirebaseDatabase.getInstance()
        val scoresRef = database.getReference("Puntuaciones").child("MEMORIA")
        scoresRef.orderByChild("tiempo").limitToFirst(20).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                scores.clear()
                for (scoreSnapshot in dataSnapshot.children) {
                    val score = scoreSnapshot.getValue(Score::class.java)
                    if (score != null) {
                        scores.add(score)
                    }
                }
                scoresAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
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
                Toast.makeText(this, "Has cerrado sesión correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
