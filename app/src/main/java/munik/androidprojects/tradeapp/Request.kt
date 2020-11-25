package munik.androidprojects.tradeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
private lateinit var auth: FirebaseAuth
private lateinit var prooced_button : Button
private var cunter : Int = 0;
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
        val db = FirebaseFirestore.getInstance()
        name_of_item = view.findViewById(R.id.name_of_request)
        quantity = view.findViewById(R.id.sizeInMeters_request)
        price = view.findViewById(R.id.price_request)
        prooced_button = view.findViewById(R.id.prooceed_request)
        auth = FirebaseAuth.getInstance()

        prooced_button.setOnClickListener {
            db.collection("items").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val a = document.data.get("name_of_item") as String
                    val b = document.data.get("price") as String
                    val c = document.data.get("quantity") as String
                    if((a.equals(name_of_item.text.toString())) && (b.equals(price.text.toString())) && (c.equals(
                            quantity.text.toString()))) {
                        cunter = 1;
                        Toast.makeText(context, "the item is available", Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "prooceed for payment", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(context,ItemListPageAfterLoginUser::class.java))
                        break
                    }

                    //val k = a.toString().substring(10,a.toString().length-1)
                   // Log.d("info", "get failed with =" + document.data.toString())

                }
                if( cunter == 0){
                    Toast.makeText(context, "out of stok", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context,ItemListPageAfterLoginUser::class.java))
                }

            }.addOnFailureListener { exception ->
                Log.d("info", "get failed with ", exception)

            }
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