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

class MainViewModel : ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<UserModel>>()

    fun setListUser(query: String) {
        val listData = ArrayList<UserModel>()

        val url = "https://api.github.com/search/users?q=$query&page=1&per_page=100"

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
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = UserModel()
                        user.idNumber = item.getInt("id")
                        user.username = item.getString("login")
                        user.userUrl = item.getString("html_url")
                        user.avatarUrl = item.getString("avatar_url")
                        listData.add(user)
                    }
                    listUsers.postValue(listData)

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
    fun getUsers(): LiveData<ArrayList<UserModel>> = listUsers
}