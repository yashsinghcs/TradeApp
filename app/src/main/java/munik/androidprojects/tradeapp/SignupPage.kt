package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.nio.charset.MalformedInputException

class SignupPage : AppCompatActivity() {

    private lateinit var loginButton_SignupPage: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var password : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        //initializing the View class object
        loginButton_SignupPage = findViewById(R.id.verify_button_OTP)
        email = findViewById(R.id.username_signUp)
        password = findViewById(R.id.password_signUp)
        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            startActivity(Intent(applicationContext, MalformedInputException::class.java))
            finish()
        }
        loginButton_SignupPage.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser(){
        if (email.text.toString().isEmpty()) {
            email.error = "please enter username"
            email.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "please enter password"
            password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                Toast.makeText(baseContext,"account created succsesfully",Toast.LENGTH_SHORT).show();
                startActivity(Intent(this,LoginPage::class.java))
                finish()
            }
            else{
                Toast.makeText(baseContext,"Signup Failed.Try again after some time",Toast.LENGTH_SHORT).show();
            }
        }
    }
}