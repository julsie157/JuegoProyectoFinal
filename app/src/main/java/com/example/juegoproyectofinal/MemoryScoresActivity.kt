package com.example.juegoproyectofinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MemoryScoresActivity : AppCompatActivity() {

    private lateinit var scoresList: RecyclerView
    private lateinit var scoresAdapter: ScoresAdapter
    private lateinit var scores: MutableList<Score>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        scoresList = findViewById(R.id.scoresList)
        scores = mutableListOf()
        scoresAdapter = ScoresAdapter(scores)
        scoresList.adapter = scoresAdapter
        scoresList.layoutManager = LinearLayoutManager(this)

        fetchScores()
    }

    private fun fetchScores() {
        val database = FirebaseDatabase.getInstance()
        val scoresRef = database.getReference("scores").child("MEMORY")
        scoresRef.orderByChild("time").limitToFirst(20).addValueEventListener(object : ValueEventListener {
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
                Toast.makeText(this@MemoryScoresActivity, "Error al cargar las puntuaciones", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

