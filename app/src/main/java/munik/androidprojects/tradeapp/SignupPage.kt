package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.nio.charset.MalformedInputException

class SignupPage : AppCompatActivity() {

    private lateinit var loginButton_SignupPage: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var re_enter_password : EditText
    private lateinit var phoneNo : EditText
    private lateinit var e_mail : EditText
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        //initializing the View class object
        loginButton_SignupPage = findViewById(R.id.verify_button_OTP)
        email = findViewById(R.id.username_signUp)
        password = findViewById(R.id.password_signUp)
        re_enter_password = findViewById(R.id.reEnterPassword_signUp)
        phoneNo = findViewById(R.id.Phoneno_signUp)
        e_mail = findViewById(R.id.Email_signUp)

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
                doAddition()
                Toast.makeText(baseContext,"account created succsesfully",Toast.LENGTH_SHORT).show();
                startActivity(Intent(this,LoginPage::class.java))
                finish()
            }
            else{
                Toast.makeText(baseContext,"Signup Failed.Try again after some time",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun doAddition() {
        if (re_enter_password.text.toString().isEmpty()) {
            re_enter_password.error = "please enter enterbookname"
            re_enter_password.requestFocus()
            return
        }
        if (phoneNo.text.toString().isEmpty()) {
            phoneNo.error = "please enter phoneNo"
            phoneNo.requestFocus()
            return
        }
        if (e_mail.text.toString().isEmpty()) {
            e_mail.error = "please enter email"
            e_mail.requestFocus()
            return
        }
        val userdetails: MutableMap<String, Any> = HashMap()
        userdetails["phoneNo"] = phoneNo.text.toString()
        userdetails["email"] = e_mail.text.toString()
        userdetails["username"] = email.text.toString()
        db.collection("userdetails")
            .add(userdetails)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(baseContext,"added succsesfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
            }
    }
}