package munik.androidprojects.tradeapp;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.chaos.view.PinView
//import com.goodiebag.pinview.Pinview
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import java.util.concurrent.TimeUnit

class EnteringOTP : AppCompatActivity() {
    private lateinit var firebseAuth : FirebaseAuth
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private lateinit var token : PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var  verification : String
    private lateinit var resendOTP : TextView
    private lateinit var phonenumber : String
    private lateinit var name : String
    private lateinit var e_mail : String
    private lateinit var unique_id : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_o_t_p);


        val intent : Intent = intent
        phonenumber = intent.getStringExtra("phone") as String
        name = intent.getStringExtra("name") as String
        e_mail = intent.getStringExtra("email") as String
        unique_id = intent.getStringExtra("unique_id") as String
       // FirebaseAuth.getInstance().signOut();
        val pinview = findViewById<EditText>(R.id.pinView)
        val verify = findViewById<Button>(R.id.sighupverify_button_OTP)
        //FirebaseAuth.getInstance().signOut();
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
        verify.setOnClickListener{
            Toast.makeText(baseContext,"pin value"+pinview.text.toString(),Toast.LENGTH_SHORT).show()

            val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verification,pinview.text.toString())
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
        firebseAuth.signInWithCredential(credential).addOnSuccessListener {
            Toast.makeText(baseContext,"account created successfully",Toast.LENGTH_SHORT).show()
            doAddition()
            startActivity(Intent(baseContext,LoginPage::class.java))
            //FirebaseAuth.getInstance().signOut();
        }
    }
    private fun doAddition() {

        val userdetails: MutableMap<String, Any> = HashMap()
        userdetails["phoneNo"] = phonenumber
        userdetails["email"] = e_mail
        userdetails["name"] = name
        userdetails["unique_id"] = unique_id
        //Toast.makeText(Signup_student.this, "user created", Toast.LENGTH_SHORT).show();
        //putting other data like name ,email etc into the fire base collection name users

        //Toast.makeText(Signup_student.this, "user created", Toast.LENGTH_SHORT).show();
        //putting other data like name ,email etc into the fire base collection name users
        val userId_techer = firebseAuth.getCurrentUser()?.getUid()
        val documentReference: DocumentReference =
            db.collection("PHONE_STUDENT").document(userId_techer as String)
        documentReference.set(userdetails)
            .addOnSuccessListener { // Log.i("info", "on success:user  profile is created" + userId_techer);
                //. Log.i("info","on success:user  profile is created"+userId);
                //progressBar.visibility = View.GONE
                //loginButton_SignupPage.isEnabled = true

                Toast.makeText(baseContext,"added succsesfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                //progressBar.visibility = View.GONE
                //loginButton_SignupPage.isEnabled = true

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