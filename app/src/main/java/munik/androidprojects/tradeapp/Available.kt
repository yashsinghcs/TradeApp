package munik.androidprojects.tradeapp

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Available.newInstance] factory method to
 * create an instance of this fragment.
 */
private lateinit var name_of_item : EditText
private lateinit var quantity :EditText
private lateinit var price : EditText
private lateinit var auth: FirebaseAuth
private lateinit var prooced_button : Button
private lateinit var item_image : ImageView
lateinit var currentPhotoPath: String
private lateinit var  firebaseStorage: StorageReference
private lateinit var  returnUri :Uri
var db = FirebaseFirestore.getInstance()
class Available : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_available, container, false)

        name_of_item = view.findViewById(R.id.name_of_text)
        item_image = view.findViewById(R.id.imageofthe_item_available)
        quantity = view.findViewById(R.id.sizeInMeters_availsble)
        price = view.findViewById(R.id.price)
        prooced_button = view.findViewById(R.id.prooceed)
        auth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance().getReference()

        prooced_button.setOnClickListener {
            val userdetails: MutableMap<String, Any> = HashMap()
            userdetails["name_of_item"] = name_of_item.text.toString()
            userdetails["quantity"] = quantity.text.toString()
            userdetails["price"] = price.text.toString()

            db.collection("items")
                .add(userdetails)
                .addOnSuccessListener { documentReference ->
                    var k : String  = "" + name_of_item.text.toString() + "" + quantity.text.toString() + "" + price.text.toString()
                    val image1: StorageReference = firebaseStorage.child("items_available/"+k)
                    image1.putFile(returnUri).addOnSuccessListener {
                        Toast.makeText(activity, "Image is uploaded", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(activity,"added succsesfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity,"not added due to some reason please try again", Toast.LENGTH_SHORT).show()
                }
            startActivity(Intent(context,ItemListPageAfterLoginUser::class.java))
        }

        item_image.setOnClickListener{

           if(context?.let { it1 -> ActivityCompat.checkSelfPermission(it1,android.Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
               requestPermissions(
                   arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                   2000);
           }
           else {
               startGallery();
           }
        }
        return view
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === RESULT_OK) {
            if (requestCode == 1000) {
                 returnUri = data!!.data!!
                val bitmapImage: Bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                item_image.setImageBitmap(bitmapImage)
            }
        }
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Available.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Available().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}