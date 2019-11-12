package org.wit.placemark.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.UserModel

class LoginActivity : AppCompatActivity(),View.OnClickListener{

    private var isLogin = false
    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp
        initializelistener()
    }

    private fun initializelistener() {
        btn_login.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       if (p0 == btn_login){
         login()
       }
        else if (p0 == btn_signup){
           signUp()
       }
    }

    private fun login() {
         user.email = useremail.text.toString()
         user.password = userPass.text.toString()

        if (!validateEditText(useremail.text.toString())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }
        else if (!isValidEmail(useremail.text.toString())){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
        }
        else if (!validateEditText(userPass.text.toString())) {
            Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show()
        }
        else if (app.users.login(user)){
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
        else {
            // Intent intent = new Intent(this,Video.class);
            //startActivity(intent);
            startActivity(intentFor<Dashboard>())
            overridePendingTransition(
                R.anim.abc_fade_in,
                R.anim.abc_fade_out
            )
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        }

        useremail.setText("")
        userPass.setText("")
    }

    fun validateEditText(text: String): Boolean {
        return !TextUtils.isEmpty(text) && text.trim { it <= ' ' }.length > 0
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signUp() {
        user.email = useremail.text.toString()
        user.password = userPass.text.toString()


        if (!validateEditText(useremail.text.toString())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }else if(!isValidEmail(useremail.text.toString())){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
        }
        else if (!validateEditText(userPass.text.toString())) {
            Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show()
        }
        else if(app.users.signup(user)){
            Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show()
        }
        else {
            app.users.create(user.copy())
            Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show()
        }

        useremail.setText("")
        userPass.setText("")
    }

}
