package com.mosfl.example.logindemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mosfl.example.logindemo.model.LoginInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.regex.Pattern

class ValidationRepository {

    // MARK: Declaration of Composite Disposable
    private var mCompositeDisposable: CompositeDisposable? = null

    init {
        // MARK: Initialising the composite Disposable
        // Note: We use composite disposable if we want to make more than one Disposables, then we can add them into Composite Disposable.
        mCompositeDisposable = CompositeDisposable()
    }

    fun validateCredentials(emailID: String, password: String): LiveData<LoginInfo> {
        val loginErrorMessage = MutableLiveData<LoginInfo>()
        if (isEmailValid(emailID)) {
            if (password.length < 8 && !isPasswordValid(password)) {
                loginErrorMessage.value = LoginInfo(userName = "Error", userPassWord = "Test", isValidInfo = false)
            } else {
                return providesWebService()
            }
        } else {
            loginErrorMessage.value = LoginInfo(userName = "Error", userPassWord = "Test", isValidInfo = false)
        }
        return loginErrorMessage
    }

    private fun isEmailValid(email: String): Boolean {
        // MARK: This function will validate the user name.
        // Currently it is validing the username with the regular expression of Email address.
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        // MARK: This function will validate the password.
        val expression = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\\\S+\$).{4,}\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun providesWebService(): LiveData<LoginInfo> {
        //MARK: This function is making an API call to the server when credentials are validated.
        val data = MutableLiveData<LoginInfo>()
        try {
            // MARK: Here we have create the Builder variable of Retrofit.
            // Here the retrofit will return the Call adapter in the form of RxJAVA's Observable<> call adapter form.
            val retrofit = Retrofit.Builder()
                .baseUrl(APIURL.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(APIService::class.java)

            // MARK: Currently we have added only one subscription of retrofit in the Composite Disposable.
            // Later we can add more subscription in the same Disposable.
            mCompositeDisposable?.add(
                retrofit.makeRequest()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response: String? ->
                        data.setValue(parseJson(response))
                    }, {
                        val errorInfo = LoginInfo(
                            userName = "Error Occured",
                            userPassWord = "No Password",
                            isValidInfo = false
                        )
                        data.setValue(errorInfo)
                    })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private fun parseJson(response: String?): LoginInfo {
        // MARK: This function is used to parse the JSON response received from the API call.
        // Depending on the response the respective method is used to return the data.
        val jsonArray: JSONArray
        val loginInfo = LoginInfo(userName = "Error Occured", userPassWord = "No Password", isValidInfo = false)
        try {
            jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                var jsonInfo: JSONObject = jsonArray.getJSONObject(i)
                val mLoginInfo = LoginInfo(
                    userName = jsonInfo.getString("title"),
                    userPassWord = jsonInfo.getString("body"),
                    isValidInfo = true
                )
                return mLoginInfo
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return loginInfo
    }
}
