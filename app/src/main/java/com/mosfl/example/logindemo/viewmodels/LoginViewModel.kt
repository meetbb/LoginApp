package com.mosfl.example.logindemo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mosfl.example.logindemo.model.LoginInfo
import com.mosfl.example.logindemo.repository.ValidationRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    //MARK: Repository declaration
    private var validationRepository: ValidationRepository = ValidationRepository()

    /*
    * Function that calls the repository's validation function, meanwhile the same function is also been observed on View.
    * This function will return the Mutable Live data of LoginInfo(model class)
    * */
    fun validateCredentials(email: String, passWord: String): LiveData<LoginInfo> {
        return validationRepository.validateCredentials(email, passWord)
    }
}