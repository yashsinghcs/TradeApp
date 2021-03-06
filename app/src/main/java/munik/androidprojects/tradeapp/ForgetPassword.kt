package munik.androidprojects.tradeapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var username : EditText
    private lateinit var mobileno : EditText
    private lateinit var email : EditText
    private lateinit var restbutton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        username=findViewById(R.id.username_forgetpassword)
        mobileno=findViewById(R.id.Phoneno_forgetpassword)
        email=findViewById(R.id.Email_forgetpassword)
        restbutton=findViewById(R.id.forgotPassword_button_OTP)
        auth = FirebaseAuth.getInstance()
        restbutton.setOnClickListener{
            reset()
        }


    }
    fun reset(){
        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("sucess", "Email sent.")
                    Toast.makeText(baseContext, "recovery mail sent", Toast.LENGTH_SHORT).show();
                    finish()
                }
                else{
                    Toast.makeText(baseContext,"some error occured", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
