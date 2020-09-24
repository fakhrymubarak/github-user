package com.fakhry.githubusersubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhry.githubusersubmission.BuildConfig
import com.fakhry.githubusersubmission.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel : ViewModel() {
    private val dataDetails = MutableLiveData<UserModel>()

    fun setDataUser(username: String?) {
        val url = "https://api.github.com/users/$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val user = UserModel()
                    user.avatarUrl = responseObject.getString("avatar_url")
                    user.username = responseObject.getString("login")
                    user.name = responseObject.getString("name")
                    user.company = responseObject.getString("company")
                    user.location = responseObject.getString("location")
                    user.repository = responseObject.getInt("public_repos")
                    user.followers = responseObject.getInt("followers")
                    user.following = responseObject.getInt("following")
                    user.bio = responseObject.getString("bio")
                    dataDetails.postValue(user)
                } catch (e: Exception) {
                    Log.d("onSuccess", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }
    fun getDetailUser(): LiveData<UserModel> = dataDetails
}