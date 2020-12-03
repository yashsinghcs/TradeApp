package munik.androidprojects.tradeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.nio.charset.MalformedInputException

class SignupPage : AppCompatActivity() {

    private lateinit var loginButton_SignupPage: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var name : EditText
    private lateinit var password : EditText
    private lateinit var re_enter_password : EditText
    private lateinit var phoneNo : EditText
    private lateinit var e_mail : EditText
    private lateinit var progressBar : ProgressBar

    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        //initializing the View class object

        loginButton_SignupPage = findViewById(R.id.sighupverify_button_OTP)
        name = findViewById(R.id.username_signUp)
        password = findViewById(R.id.password_signUp)
        re_enter_password = findViewById(R.id.reEnterPassword_signUp)
        phoneNo = findViewById(R.id.Phoneno_signUp)
        e_mail = findViewById(R.id.Email_signUp)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            startActivity(Intent(applicationContext, MalformedInputException::class.java))
            finish()
        }
        loginButton_SignupPage.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (name.text.toString().isEmpty()) {
            name.error = "please enter name"
            name.requestFocus()
            return
        }
        if (e_mail.text.toString().isEmpty()) {
            e_mail.error = "please enter email"
            e_mail.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "please enter password"
            password.requestFocus()
            return
        }
        if (password.text.toString().length < 7) {
            password.error = "password length must be 6 and above"
            password.requestFocus()
            return

        }
        if (re_enter_password.text.toString().isEmpty()) {
            re_enter_password.error = "re enter the password"
            re_enter_password.requestFocus()
            return

        }
        if (phoneNo.text.toString().isEmpty()) {
            phoneNo.error = "enter the password"
            phoneNo.requestFocus()
            return

        }
        if (password.text.toString().equals(re_enter_password.text.toString())) {

        }
        else{
            password.error = "password and re_enter password mismatched"
            re_enter_password.error = "password and re_enter password mismatched"
            password.requestFocus()
            re_enter_password.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE
        loginButton_SignupPage.isEnabled = false
        if (password.text.toString().equals(re_enter_password.text.toString())) {
            auth.createUserWithEmailAndPassword(e_mail.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        doAddition()
                        progressBar.visibility = View.GONE
                        loginButton_SignupPage.isEnabled = true
                        Toast.makeText(
                            baseContext,
                            "account created succsesfully",
                            Toast.LENGTH_SHORT
                        ).show();
                        val goToOtpPage : Intent = Intent(this,EnteringOTP::class.java)
                        goToOtpPage.putExtra("phone","+91"+phoneNo.text.toString())
                        goToOtpPage.putExtra("email",e_mail.text.toString())
                        goToOtpPage.putExtra("name",name.text.toString())
                        startActivity(goToOtpPage)
                        finish()
                    } else {
                        progressBar.visibility = View.GONE
                        loginButton_SignupPage.isEnabled = true
                        Toast.makeText(
                            baseContext,
                            "Signup failed please check and try again"+(task.exception?.message ?: String),
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        }

    }

    private fun doAddition() {

        val userdetails: MutableMap<String, Any> = HashMap()
        userdetails["phoneNo"] = phoneNo.text.toString()
        userdetails["email"] = e_mail.text.toString()
        userdetails["name"] = name.text.toString()

        //Toast.makeText(Signup_student.this, "user created", Toast.LENGTH_SHORT).show();
        //putting other data like name ,email etc into the fire base collection name users

        //Toast.makeText(Signup_student.this, "user created", Toast.LENGTH_SHORT).show();
        //putting other data like name ,email etc into the fire base collection name users
        val userId_techer = auth.getCurrentUser()?.getUid()
        val documentReference: DocumentReference =
            db.collection("STUDENT").document(userId_techer as String)
        documentReference.set(userdetails)
            .addOnSuccessListener { // Log.i("info", "on success:user  profile is created" + userId_techer);
                //. Log.i("info","on success:user  profile is created"+userId);
                progressBar.visibility = View.GONE
                loginButton_SignupPage.isEnabled = true

                Toast.makeText(baseContext,"added succsesfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                loginButton_SignupPage.isEnabled = true

                Toast.makeText(baseContext,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
            }
        /*db.collection("userdetails")
            .add(userdetails)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(baseContext,"added succsesfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
            }*/
    }
}