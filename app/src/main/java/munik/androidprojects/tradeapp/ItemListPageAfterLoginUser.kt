package munik.androidprojects.tradeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_item_list_page_after_login_user.*

class ItemListPageAfterLoginUser : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var searchView: SearchView
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerlayout : DrawerLayout
    lateinit var navView : NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list_page_after_login_user)

        //initializing the objects
        recyclerView = findViewById(R.id.recycler_view_item) as RecyclerView
        searchView = findViewById(R.id.searchView) as SearchView
        drawerlayout = findViewById(R.id.drawerlayout_ItemListPageAfterLoginUser)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this,drawerlayout,R.string.open,R.string.close)

        recyclerView.layoutManager = LinearLayoutManager(this)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = ArrayList<dataModel>()
        user . add(dataModel("balaram"))
        user . add(dataModel("ishita"))
        user . add(dataModel("goggo"))
        user . add(dataModel("billi"))
        user . add(dataModel("ballu"))
        user . add(dataModel("akshat"))
        user . add(dataModel("dubeyji"))
        user . add(dataModel("himanshu"))
        user . add(dataModel("willy"))
        user . add(dataModel("ballu"))
        val adapter = customeAdapter(user)
        recyclerView.adapter = adapter


        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true

        }
        return super.onOptionsItemSelected(item)
    }
}