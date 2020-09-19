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
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {
    private val listFollowing = MutableLiveData<ArrayList<UserModel>>()

    fun setListFollowing(username: String) {
        val listData = ArrayList<UserModel>()

        val url = "https://api.github.com/users/$username/following?page=1&per_page=100"

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
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val userModel = UserModel()
                        userModel.username = item.getString("login")
                        userModel.userUrl = item.getString("html_url")
                        userModel.avatarUrl = item.getString("avatar_url")
                        listData.add(userModel)
                    }
                    listFollowing.postValue(listData)
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

    fun getUsers(): LiveData<ArrayList<UserModel>> = listFollowing
}