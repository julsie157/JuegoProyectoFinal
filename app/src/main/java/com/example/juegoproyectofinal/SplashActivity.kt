package com.example.juegoproyectofinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animationView = findViewById<LottieAnimationView>(R.id.animationView)
        val titleText = findViewById<TextView>(R.id.titleText)

        // Cargar la animación para el texto
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        titleText.startAnimation(zoomIn)

        // Configurar el splash screen para que dure 3 segundos y luego inicie el LoginActivity
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
