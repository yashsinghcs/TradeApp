package munik.androidprojects.tradeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var recyclerView : RecyclerView
private lateinit var searchView: SearchView
private lateinit var adapter :customeAdapter_admin
private lateinit var item_image : ImageView
private lateinit var mStoragereference1: StorageReference
private lateinit var bitmap1 : Bitmap
/**
 * A simple [Fragment] subclass.
 * Use the [Available_admin.newInstance] factory method to
 * create an instance of this fragment.
 */
class Available_admin : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_available_admin, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_item_admin) as RecyclerView
        searchView = view.findViewById(R.id.searchView_admin) as SearchView
        item_image = view.findViewById(R.id.imageofthe_item_available)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val user = ArrayList<dataModel>()
        db.collection("items").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val a = document.data.get("name_of_item") as String
                val b = document.data.get("price") as String + "/-"
                val c = document.data.get("quantity") as String + "m"

                //val k = a.toString().substring(10,a.toString().length-1)
                Log.d("info", "get failed with =" + document.data.toString())
                var k : String = "" + a + "" + c + "" + b
                mStoragereference1 = FirebaseStorage.getInstance().reference
                    .child("items_available/" + k)
                try {
                    val file = File.createTempFile("image", "jpg")
                    mStoragereference1.getFile(file)
                        .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                            var bitmap1 =
                                BitmapFactory.decodeFile(file.absolutePath)
                            /* (findViewById<View>(R.id.profile_image) as ImageView).setImageBitmap(
                                 bitmap
                             )
                             profilePic.setImageBitmap(bitmap)*/
                        })
                } catch (e: IOException) {
                    e.printStackTrace()
                }
               // user.add(dataModel(a,b ,c, bitmap1))
            }
            adapter = customeAdapter_admin(user)
            recyclerView.adapter = adapter
        }.addOnFailureListener { exception ->
            Log.d("info", "get failed with ", exception)

        }

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Available_admin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Available_admin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}