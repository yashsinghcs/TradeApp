package munik.androidprojects.tradeapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Request.newInstance] factory method to
 * create an instance of this fragment.
 */
private lateinit var name_of_item : EditText
private lateinit var quantity : EditText
private lateinit var price : EditText
private lateinit var user : FirebaseUser
private lateinit var fAuth : FirebaseAuth
private lateinit var prooced_button : Button
private lateinit var name : String
private lateinit var email : String
private lateinit var phone : String

class Request : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request, container, false)
        name_of_item = view.findViewById(R.id.name_of_request)
        quantity = view.findViewById(R.id.sizeInMeters_request)
        price = view.findViewById(R.id.price_request)
        prooced_button = view.findViewById(R.id.prooceed_request)
        fAuth = FirebaseAuth.getInstance()
        user = fAuth.getCurrentUser()!!;
        Toast.makeText(context,"email "+ user.uid, Toast.LENGTH_SHORT).show()
        val docRef =
            db.collection("STUDENT").document(user.getUid())
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    name  = document.data?.get("name").toString()
                    email  = document.data?.get("email").toString()
                    phone  = document.data?.get("phoneNo").toString()
                }else {
                    Toast.makeText(context,"email not retrived", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("info", "get failed with ", task.exception)
            }
        }
        prooced_button.setOnClickListener {
            val userdetails: MutableMap<String, Any> = HashMap()
            userdetails["name_of_item"] = name_of_item.text.toString()
            userdetails["quantity"] = quantity.text.toString()
            userdetails["price"] = price.text.toString()
            userdetails["name"] = name
            userdetails["email"] = email
            userdetails["phone"] = phone
            userdetails["check"] = "0"
            db.collection("items_request")
                .add(userdetails)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(activity,"added succsesfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
                }
            startActivity(Intent(context,ItemListPageAfterLoginUser::class.java))


            startActivity(Intent(context,ItemListPageAfterLoginUser::class.java))

        }
        return  view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Request.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Request().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}