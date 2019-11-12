package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.UserModel

class Dashboard : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(dashboardToolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, dashboardToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        app = application as MainApp

        user = app.users.getLoggedIn()

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationview.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.add -> startActivityForResult<PlacemarkActivity>(200)
            R.id.delete -> delete()
            R.id.list -> startActivityForResult<PlacemarkListActivity>(200)
            R.id.logout ->  logout()
        }
        drawer.closeDrawer(GravityCompat.START)
        return  true
    }

    private fun logout(){
        app.users.logOut()
        startActivity(intentFor<LoginActivity>())
    }

    private fun delete(){
        app.users.delete(user)
        startActivity(intentFor<LoginActivity>())
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
