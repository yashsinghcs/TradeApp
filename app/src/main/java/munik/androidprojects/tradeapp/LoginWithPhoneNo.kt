package munik.androidprojects.tradeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import java.util.concurrent.TimeUnit

class LoginWithPhoneNo : AppCompatActivity() {
    private  lateinit var verify : Button
    private lateinit var phone : EditText
    private lateinit var OTP : EditText
    private lateinit var firebseAuth : FirebaseAuth
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var  verification : String
    private lateinit var token : PhoneAuthProvider.ForceResendingToken
    private lateinit var fuser : FirebaseUser
    private  var count : Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_phone_no)

        verify = findViewById(R.id.login_phone)
        phone  = findViewById(R.id.phone_no)
        OTP = findViewById(R.id.otp_no)
        firebseAuth = FirebaseAuth.getInstance()

        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(baseContext,"Verification Failed"+p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verification = p0
                token = p1
                OTP.visibility = View.VISIBLE

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }


        }
        verify.setOnClickListener {
            if(count == 0) {
                sendOTP("+91"+phone.text.toString())
                count = 1
            }else {
                val credential: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(verification, OTP.text.toString())
                verifyAuthentication(credential)
                count = 0
            }
        }
    }

    private fun sendOTP(phonenumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,60L,
            TimeUnit.SECONDS,this,mCallbacks)

    }

    private fun verifyAuthentication(credential: PhoneAuthCredential){
        firebseAuth.signInWithCredential(credential).addOnSuccessListener {

            val userId_techer = firebseAuth.getCurrentUser()?.getUid()
            val documentReference: DocumentReference =
                db.collection("PHONE_STUDENT").document(userId_techer as String)

            fuser = firebseAuth.getCurrentUser()!!

            documentReference.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                       startActivity(Intent(baseContext,ItemListPageAfterLoginUser::class.java))
                        Toast.makeText(baseContext,"logined successful",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(baseContext, "userDoesnotExist", Toast.LENGTH_SHORT)
                            .show()
                        fuser.delete().addOnCompleteListener {task ->
                            if (task.isSuccessful) {

                                // Toast.makeText(phn_teacher.this,"noDeleted",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut()
                                startActivity(
                                    Intent(
                                        baseContext,
                                        LoginWithPhoneNo::class.java
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}