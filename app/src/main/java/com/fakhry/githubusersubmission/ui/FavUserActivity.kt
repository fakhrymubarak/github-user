package com.fakhry.githubusersubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.adapter.FavUserAdapter
import com.fakhry.githubusersubmission.db.FavUserHelper
import com.fakhry.githubusersubmission.helper.MappingHelper
import com.fakhry.githubusersubmission.model.UserModel
import kotlinx.android.synthetic.main.activity_fav_user.*
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

        supportActionBar?.title = getString(R.string.title_fav_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_fav_user.layoutManager = LinearLayoutManager(this)
        rv_fav_user.setHasFixedSize(true)
        adapter = FavUserAdapter()

        favUserHelper = FavUserHelper.getInstance(applicationContext)
        favUserHelper.open()

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
                showSelectedUser(data)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSelectedUser(userModel: UserModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STATE, userModel)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val favUser =
                        data.getParcelableExtra<UserModel>(DetailActivity.EXTRA_FAVORITE)
                    adapter.addItem(favUser!!)
                    rv_fav_user.smoothScrollToPosition(adapter.itemCount - 1)
                }
                DetailActivity.REQUEST_DELETE ->
                    if (resultCode == DetailActivity.RESULT_DELETE) {
                        val position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                        adapter.removeItem(position)
                    }
            }
        }
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
                val cursor = favUserHelper.queryAll()
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