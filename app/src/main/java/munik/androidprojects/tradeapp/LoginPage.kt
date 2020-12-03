package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.nio.charset.MalformedInputException

class LoginPage : AppCompatActivity() {
    private lateinit var signup_button_LoginPage:TextView
    private lateinit var forgetpassword:TextView
    private lateinit var loginbutton : Button
    private lateinit var Auth: FirebaseAuth
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login_phone : Button
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //initialising the View objects.
        signup_button_LoginPage = findViewById(R.id.signup)
        forgetpassword = findViewById(R.id.forgetpassword_textview)
        loginbutton = findViewById(R.id.login)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
        login_phone = findViewById(R.id.loginWith_phone)
        progressBar = findViewById(R.id.progressBar2)
        Auth=FirebaseAuth.getInstance()
        //FirebaseAuth.getInstance().signOut();
       /* if (Auth!!.currentUser != null) {
            startActivity(Intent(applicationContext, LoginPage::class.java))
            finish()
        }*/
        signup_button_LoginPage.setOnClickListener {
            startActivity(Intent(this,SignupPage::class.java))
        }

        forgetpassword.setOnClickListener {
            startActivity( Intent(this,ForgetPassword::class.java))
        }

        loginbutton.setOnClickListener {
            doLogin()
            //startActivity( Intent(this,ItemListPageAfterLoginUser::class.java))

        }

        login_phone.setOnClickListener{
            startActivity(Intent(this,LoginWithPhoneNo::class.java))
        }
    }

    private fun doLogin(){

        if (username.text.toString().isEmpty()) {
            username.error = "please enter username"
            username.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "please enter password"
            password.requestFocus()
            return
        }
        if (password.text.toString().length < 6) {
            password.error = "please enter proper password"
            password.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE
        loginbutton.isEnabled = false
        login_phone.isEnabled = false
        signup_button_LoginPage.isEnabled = false
        forgetpassword.isEnabled = false
        Auth.signInWithEmailAndPassword(username.text.toString(),password.text.toString()).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                val user: FirebaseUser?=Auth.currentUser
                updateUI(user)
            }

            else{

                updateUI(null)
            }
        }

    }
    fun updateUI(currentuser:FirebaseUser?){
        if(currentuser!=null){
            progressBar.visibility = View.GONE
            loginbutton.isEnabled = true
            login_phone.isEnabled = true
            signup_button_LoginPage.isEnabled = true
            forgetpassword.isEnabled = true
            Toast.makeText(baseContext,"Login sucsessful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ItemListPageAfterLoginUser::class.java)
            startActivity(intent)
            finish()
        }
        else{
            /*if(checker == 0){
                checker = 1
            }else {
                Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
            }*/
            Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            loginbutton.isEnabled = true
            login_phone.isEnabled = true
            signup_button_LoginPage.isEnabled = true
            forgetpassword.isEnabled = true
        }

    }

    public override fun onStart() {
        super.onStart()
        val user:FirebaseUser?=Auth.currentUser
        //updateUI(user)
        if(user!=null){
            Toast.makeText(baseContext,"Login sucsessful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,ItemListPageAfterLoginUser::class.java))
            finish()
        }

    }
}