package com.mosfl.example.logindemo.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mosfl.example.logindemo.R
import com.mosfl.example.logindemo.model.LoginInfo
import com.mosfl.example.logindemo.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    lateinit var userIdEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVars()
    }

    private fun initVars() {
        /*
        * Function to initialise variables
        */
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        userIdEditText = findViewById(R.id.userIdEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        // Click Listener of Login Button
        if (view == loginButton) {
            val emailString: String = userIdEditText.text.toString()
            val passwordString: String = userIdEditText.text.toString()
            //Passing the string parameters in the View model function and observing the response Live Data.
            loginViewModel.validateCredentials(emailString, passwordString).observe(this,
                Observer<LoginInfo> { t ->
                    Log.e("VALIDATION", "response is: ${t!!.userName}")
                    if (t.isValidInfo) {
                        // If response is Valid then it will take username data to the next screen and print there.
                        val intent = Intent(this, InfoActivity::class.java)
                        intent.putExtra("USER_NAME", t.userName)
                        startActivity(intent)
                    } else {
                        // If not valid then notify the user about invalid credentials.
                        Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}
