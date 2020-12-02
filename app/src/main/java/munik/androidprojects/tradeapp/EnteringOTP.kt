package munik.androidprojects.tradeapp;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.goodiebag.pinview.Pinview
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnteringOTP : AppCompatActivity() {
    private lateinit var firebseAuth : FirebaseAuth
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private lateinit var token : PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var  verification : String
    private lateinit var resendOTP : TextView
    private lateinit var phonenumber : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_o_t_p);


        val intent : Intent = intent
        phonenumber = intent.getStringExtra("phone") as String

        val pinview = findViewById<Pinview>(R.id.pinView)
        firebseAuth = FirebaseAuth.getInstance()
        resendOTP = findViewById(R.id.resendOtp)
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(baseContext,"Verification Failed"+p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                resendOTP.visibility = View.GONE
                verification = p0
                token = p1

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                resendOTP.visibility = View.VISIBLE
            }


        }

        sendOTP(phonenumber)
        pinview.setPinViewEventListener{ pinview, fromUser ->
            Toast.makeText(baseContext,"pin value"+pinview.value,Toast.LENGTH_SHORT).show()

            val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verification,pinview.value)
            verifyAuthentication(credential)
        }

        resendOTP.setOnClickListener{
            resendOTP(phonenumber)
        }
    }

    private fun sendOTP(phonenumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,60L,
            TimeUnit.SECONDS,this,mCallbacks)

    }

    private fun resendOTP(phonenumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,60L,
            TimeUnit.SECONDS,this,mCallbacks,token)

    }
    private fun verifyAuthentication(credential: PhoneAuthCredential){
        firebseAuth.currentUser?.linkWithCredential(credential)?.addOnSuccessListener {
            Toast.makeText(baseContext,"account created successfully",Toast.LENGTH_SHORT).show()
        }
    }
}