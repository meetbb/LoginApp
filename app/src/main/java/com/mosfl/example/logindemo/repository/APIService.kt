package com.mosfl.example.logindemo.repository

import io.reactivex.Observable
import retrofit2.http.GET

interface APIService {
    @GET("posts")
    fun makeRequest(): Observable<String>
}