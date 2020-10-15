package munik.androidprojects.tradeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frontpage)

        //added timer so that ,after the 5 seconds of the apps opening it will go to the login page
        val timer:Timer= Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                //this method will take us to the login Activity
                goTologinPage()
            }
        }, 3500)
    }
    private fun goTologinPage(){

        val goTologinPage = Intent(this, LoginPage::class.java)
        startActivity(goTologinPage)
        finish()
    }
}