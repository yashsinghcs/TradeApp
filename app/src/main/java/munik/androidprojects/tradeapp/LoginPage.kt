package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginPage : AppCompatActivity() {
    private lateinit var signup_button_LoginPage:TextView
    private lateinit var forgetpassword:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //initialising the View objects.
        signup_button_LoginPage = findViewById(R.id.signup)
        forgetpassword = findViewById(R.id.forgetpassword_textview)

        signup_button_LoginPage.setOnClickListener {
            startActivity(Intent(this,SignupPage::class.java))
        }

        forgetpassword.setOnClickListener {
            startActivity( Intent(this,ForgetPassword::class.java))
        }
    }
}