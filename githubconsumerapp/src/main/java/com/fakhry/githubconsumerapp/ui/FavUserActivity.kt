package com.fakhry.githubconsumerapp.ui

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.githubconsumerapp.adapter.FavUserAdapter
import com.fakhry.githubconsumerapp.R
import com.fakhry.githubconsumerapp.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.fakhry.githubconsumerapp.db.FavUserHelper
import com.fakhry.githubconsumerapp.helper.MappingHelper
import com.fakhry.githubconsumerapp.model.UserModel
import kotlinx.android.synthetic.main.activity_fav_user.*
import kotlinx.android.synthetic.main.item_row_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavUserActivity : AppCompatActivity() {
    private lateinit var adapter: FavUserAdapter
    private lateinit var favUserHelper: FavUserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_user)

        supportActionBar?.title = "Github Consumer App"

        rv_fav_user.layoutManager = LinearLayoutManager(this)
        rv_fav_user.setHasFixedSize(true)
        adapter = FavUserAdapter()

        favUserHelper = FavUserHelper.getInstance(applicationContext)
        favUserHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavUser = list
                tv_opening.visibility = View.GONE
            }
        }
        rv_fav_user.adapter = adapter

        adapter.setOnItemClickCallback(object : FavUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                Toast.makeText(this@FavUserActivity,
                    getString(R.string.choose) + " " + tv_name.text,
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        favUserHelper.close()
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
        rv_fav_user.adapter = adapter
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val notes = deferredFavUser.await()
            if (notes.size > 0) {
                adapter.listFavUser = notes
            } else {
                adapter.listFavUser = ArrayList()
            }
        }
    }
}