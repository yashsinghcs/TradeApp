package munik.androidprojects.tradeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ItemListPageAfterLoginUser : AppCompatActivity() {

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
    private  var checker : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list_page_after_login_user)

        //initializing the objects
        recyclerView = findViewById(R.id.recycler_view_item) as RecyclerView
        searchView = findViewById(R.id.searchView) as SearchView
        drawerlayout = findViewById(R.id.drawerlayout_ItemListPageAfterLoginUser)
        navView = findViewById(R.id.navView)

        fAuth = FirebaseAuth.getInstance()
        user = fAuth.getCurrentUser()!!;

        var headerView : View = navView.getHeaderView(0)
        headText = headerView.findViewById(R.id.EMAILid)
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
}