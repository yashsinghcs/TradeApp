package munik.androidprojects.tradeapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_forchoosing_upload_picture.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

class ItemListPageAfterLoginUser : AppCompatActivity() {
// yash bohut bada gandu
    private lateinit var recyclerView : RecyclerView
    private lateinit var searchView: SearchView
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerlayout : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var available: Available
    lateinit var request: Request
    lateinit var messeges: Messeges
    private lateinit var adapter :customeAdapter
    private lateinit var headText : TextView
    private lateinit var user : FirebaseUser
    private lateinit var fAuth : FirebaseAuth
    private lateinit var profilePic : ImageView
    private lateinit var  firebaseStorage: StorageReference
    lateinit var currentPhotoPath: String
    private  var checker : Int = 0
    private lateinit var mStoragereference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list_page_after_login_user)

        //initializing the objects
        recyclerView = findViewById(R.id.recycler_view_item) as RecyclerView
        searchView = findViewById(R.id.searchView) as SearchView
        drawerlayout = findViewById(R.id.drawerlayout_ItemListPageAfterLoginUser)
        navView = findViewById(R.id.navView)
        firebaseStorage = FirebaseStorage.getInstance().getReference()

        fAuth = FirebaseAuth.getInstance()
        user = fAuth.getCurrentUser()!!;

        var headerView : View = navView.getHeaderView(0)
        headText = headerView.findViewById(R.id.EMAILid)
        profilePic = headerView.findViewById(R.id.profile_image)
        toggle = ActionBarDrawerToggle(this,drawerlayout,R.string.open,R.string.close)

        recyclerView.layoutManager = LinearLayoutManager(this)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




            val docRef =
                db.collection("STUDENT").document(user.getUid())

            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        headText.text = document.data?.get("email").toString()
                        //filena me = "STUDENT/"+"+-"+document.getString("name");
                        mStoragereference = FirebaseStorage.getInstance().reference
                            .child("STUDENT/" + document.getString("email"))
                        try {
                            val file = File.createTempFile("image", "jpg")
                            mStoragereference.getFile(file)
                                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                                    val bitmap =
                                        BitmapFactory.decodeFile(file.absolutePath)
                                    (findViewById<View>(R.id.profile_image) as ImageView).setImageBitmap(
                                        bitmap
                                    )
                                    profilePic.setImageBitmap(bitmap)
                                })
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        checker = 1;
                    }
                } else {
                    Log.d("info", "get failed with ", task.exception)
                }
            }
        if( checker == 0){
            val docRef =
                db.collection("PHONE_STUDENT").document(user.getUid())

            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        headText.text = document.data?.get("email").toString()
                        mStoragereference = FirebaseStorage.getInstance().reference
                            .child("STUDENT/" + document.getString("email"))
                        try {
                            val file = File.createTempFile("image", "jpg")
                            mStoragereference.getFile(file)
                                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                                    val bitmap =
                                        BitmapFactory.decodeFile(file.absolutePath)
                                    (findViewById<View>(R.id.profile_image) as ImageView).setImageBitmap(
                                        bitmap
                                    )
                                    profilePic.setImageBitmap(bitmap)
                                })
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Log.d("info", "get failed with ", task.exception)
                }
            }
        }
        val user = ArrayList<dataModel>()
       /* user . add(dataModel("balaram"))
        user . add(dataModel("ishita"))
        user . add(dataModel("goggo"))
        user . add(dataModel("billi"))
        user . add(dataModel("ballu"))
        user . add(dataModel("akshat"))
        user . add(dataModel("dubeyji"))
        user . add(dataModel("himanshu"))
        user . add(dataModel("willy"))
        user . add(dataModel("ballu"))*/

       // recyclerView.adapter = adapter
        db.collection("items").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val a = document.data.get("name_of_item") as String
                val b = document.data.get("price") as String + "/-"
                val c = document.data.get("quantity") as String + "m"

                //val k = a.toString().substring(10,a.toString().length-1)
                Log.d("info", "get failed with =" + document.data.toString())
                user.add(dataModel(a,b ,c ))
            }
             adapter = customeAdapter(user)
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

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.available -> {
                    available= Available()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, available)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    searchView.setVisibility(View.GONE)
                    recyclerView.setVisibility(View.GONE)
                }
                R.id.request ->{
                    request = Request()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, request)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    searchView.setVisibility(View.GONE)
                    recyclerView.setVisibility(View.GONE)
                }
                R.id.messeges ->{
                    messeges = Messeges()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, messeges)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    searchView.setVisibility(View.GONE)
                    recyclerView.setVisibility(View.GONE)
                }
               R.id.logout ->{
                   FirebaseAuth.getInstance().signOut();
                   Toast.makeText(baseContext, "Logged out Successfully", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this,LoginPage::class.java))
                   finish()
               }
            }
            true
        }
        profilePic.setOnClickListener{
               val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forchoosing_upload_picture,null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("Choose between camera and gallery")
            val mAlertDialog = mBuilder.show()

            mDialogView.camera_icon.setOnClickListener {
                askcamerapermission()
                mAlertDialog.dismiss()
            }

            mDialogView.gallery_icon.setOnClickListener {
                val gallery1 = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(gallery1,105)
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,ItemListPageAfterLoginUser::class.java))
    }

    private fun askcamerapermission() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 101)
        }
        else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 101) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "camera permission is required to use camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 102) {
            if(resultCode == Activity.RESULT_OK) {
                val f = File(currentPhotoPath)
                // image.setImageURI(Uri.fromFile(f));
                // imageview.setImageURI(Uri.fromFile(f));
                Log.d("info", "Absolute Url of image is" + Uri.fromFile(f))
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                    mediaScanIntent.data = Uri.fromFile(f)
                    sendBroadcast(mediaScanIntent)
                }
                uploadImagetoFirebase(f.name, Uri.fromFile(f))
            }
        }

        if(requestCode == 105) {
            if(resultCode == Activity.RESULT_OK) {
                var  contenturi : Uri? = data?.data
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imagefilename : String = "JPEG"+ timeStamp +"." +getfileExt(contenturi)
                //image.setImageURI(contenturi)
                if (contenturi != null) {
                    uploadImagetoFirebase(imagefilename, contenturi)
                }
            }

        }
    }

    private fun uploadImagetoFirebase(name: String, contentUri: Uri) {
        val pd = ProgressDialog(this)
        pd.setTitle("File uploading....!!")
        pd.show()
        val image1: StorageReference = firebaseStorage.child("STUDENT/"+headText.text)
        image1.putFile(contentUri).addOnSuccessListener {
            image1.downloadUrl.addOnSuccessListener { uri -> Picasso.get().load(uri).into(profilePic) }
            pd.dismiss()
            Toast.makeText(this, "Image is uploaded", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                val percent = (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
                pd.setMessage("Upload :" + percent.toInt() + "%")
            } }.addOnFailureListener { e: Exception? -> Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show() }
    }

    private fun getfileExt(contenturi: Uri?): Any? {
        var c : ContentResolver = contentResolver
        var mime : MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contenturi?.let { c.getType(it) })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "munik.androidprojects.tradeapp.com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 102)
                }
            }
        }
    }
}