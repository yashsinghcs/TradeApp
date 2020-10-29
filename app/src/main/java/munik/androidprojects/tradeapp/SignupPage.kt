package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignupPage : AppCompatActivity() {

    private lateinit var loginButton_SignupPage: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        //initializing the View class object
        loginButton_SignupPage = findViewById(R.id.verify_button_OTP)

        loginButton_SignupPage.setOnClickListener {
            startActivity(Intent(this, EnteringOTP::class.java))
        }
    }
}