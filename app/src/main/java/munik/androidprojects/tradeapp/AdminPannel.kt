package munik.androidprojects.tradeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminPannel : AppCompatActivity() {

    lateinit var drawerlayout : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var available: Available_admin
    lateinit var request: Request_admin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pannel)

        drawerlayout = findViewById(R.id.drawerlayout_ItemListPageAfterLoginUser_admin)
        navView = findViewById(R.id.navView_admin)

        toggle = ActionBarDrawerToggle(this,drawerlayout,R.string.open,R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.available -> {
                    available= Available_admin()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, available)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
                R.id.request ->{
                    request = Request_admin()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, request)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
                R.id.messeges ->{

                }
                R.id.logout_admin ->{
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
}