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
import java.util.regex.Matcher
import java.util.regex.Pattern

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
        if(checker(phoneNo.text.toString())){

        }else{
            phoneNo.error = "enter proper phoneNo"
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

                        progressBar.visibility = View.GONE
                        loginButton_SignupPage.isEnabled = true
                        Toast.makeText(
                            baseContext,
                            "account created succsesfully",
                            Toast.LENGTH_SHORT
                        ).show();
                        //doAddition()
                        val userdetails: MutableMap<String, Any> = HashMap()
                        userdetails["phoneNo"] = phoneNo.text.toString()
                        userdetails["email"] = e_mail.text.toString()
                        userdetails["name"] = name.text.toString()

                        val userId_techer = auth.getCurrentUser()?.getUid()
                        userdetails["unique_id"] = auth.getCurrentUser()?.getUid().toString()
                        val documentReference: DocumentReference =
                            db.collection("STUDENT").document(userId_techer as String)
                        Toast.makeText(baseContext,"testing" + documentReference.id,Toast.LENGTH_SHORT).show()
                        documentReference.set(userdetails)
                            .addOnSuccessListener { // Log.i("info", "on success:user  profile is created" + userId_techer);
                                //. Log.i("info","on success:user  profile is created"+userId);
                                progressBar.visibility = View.GONE
                                loginButton_SignupPage.isEnabled = true

                                Toast.makeText(baseContext,"added succsesfully", Toast.LENGTH_SHORT).show()

                                val goToOtpPage : Intent = Intent(this,EnteringOTP::class.java)
                                goToOtpPage.putExtra("phone","+91"+phoneNo.text.toString())
                                goToOtpPage.putExtra("email",e_mail.text.toString())
                                goToOtpPage.putExtra("name",name.text.toString())
                                goToOtpPage.putExtra("unique_id",userId_techer.toString())
                                startActivity(goToOtpPage)
                                finish()
                            }
                            .addOnFailureListener {
                                progressBar.visibility = View.GONE
                                loginButton_SignupPage.isEnabled = true

                                Toast.makeText(baseContext,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
                            }

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
    private fun checker(phone : String) : Boolean{
        val e : Pattern = Pattern.compile("[6-9][0-9]{9}")
        val m : Matcher = e.matcher(phone)
        return m.matches()
    }
}