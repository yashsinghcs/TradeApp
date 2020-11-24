package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //initialising the View objects.
        signup_button_LoginPage = findViewById(R.id.signup)
        forgetpassword = findViewById(R.id.forgetpassword_textview)
        loginbutton = findViewById(R.id.login)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
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
            startActivity( Intent(this,LoginPage::class.java))
        }

        loginbutton.setOnClickListener {
            doLogin()
            //startActivity( Intent(this,ItemListPageAfterLoginUser::class.java))

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

    private  fun updateUI(currentuser:FirebaseUser?){
        if(currentuser!=null){
            Toast.makeText(baseContext,"Login sucsessful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,ItemListPageAfterLoginUser::class.java))
            finish()
        }
        else{
            Toast.makeText(baseContext,"Login Failed", Toast.LENGTH_SHORT).show()
        }

    }

    public override fun onStart() {
        super.onStart()
        val user:FirebaseUser?=Auth.currentUser
        updateUI(user)

    }
}