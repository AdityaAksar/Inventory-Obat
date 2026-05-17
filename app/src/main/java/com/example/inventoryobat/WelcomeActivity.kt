package com.example.inventoryobat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_MS = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val tvWelcomeTitle = findViewById<TextView>(R.id.textWelcomeTitle)
        val tvWelcomeMessage = findViewById<TextView>(R.id.textWelcomeMessage)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val email = user.email.orEmpty()
            val username = email.substringBefore("@").replaceFirstChar { it.uppercaseChar() }

            tvWelcomeTitle.text = "Selamat Datang, $username!"
            tvWelcomeMessage.text = "Anda login sebagai: $email\nSemoga hari Anda menyenangkan!"
        } else {
            tvWelcomeTitle.text = "Selamat Datang!"
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, DELAY_MS)
    }
}
