package com.fakhry.githubusersubmission.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.adapter.SectionsPagerAdapter
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.AVATAR
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.ID_NUMBER
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.URL
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.USERNAME
import com.fakhry.githubusersubmission.db.FavUserHelper
import com.fakhry.githubusersubmission.helper.MappingHelper
import com.fakhry.githubusersubmission.model.UserModel
import com.fakhry.githubusersubmission.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel

    private var isFavorite = false
    private var user: UserModel? = null
    private var position: Int = 0
    private lateinit var favUserHelper: FavUserHelper


    companion object {
        const val EXTRA_STATE = "EXTRA_STATE"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_DELETE = 200
        const val RESULT_DELETE = 201
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        user = intent.getParcelableExtra(EXTRA_STATE)
        supportActionBar?.title = user?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favUserHelper = FavUserHelper.getInstance(applicationContext)
        favUserHelper.open()

        setDataDetail(user)
        setFollowersFollowing(user)

        setIsFavorite()
//        setIsFavorite2()

        fab_favorite.setOnClickListener {
            val username = user?.username
            val userUrl = user?.userUrl
            val idNumber = user?.idNumber
            val avatar = user?.avatarUrl

            val intent = Intent()
            intent.putExtra(EXTRA_FAVORITE, user)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues().apply {
                put(USERNAME, username)
                put(URL, userUrl)
                put(ID_NUMBER, idNumber)
                put(AVATAR, avatar)
            }

            if (isFavorite) {
                val result = favUserHelper.deleteByUsername(user?.username.toString())
                if (result > 0) {
                    setResult(RESULT_DELETE, intent)
                    showToast(user?.username + " " + getString(R.string.removed_from_favorite))
                } else {
                    Log.e("DetailActivity", "Failed to delete user")
                }
            } else {
                val result = favUserHelper.insert(values)
                if (result > 0) {
                    user?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    showToast(user?.username + " " + getString(R.string.added_to_favorite))
                } else {
                    Log.e("DetailActivity", "Failed to add new user")
                }
            }
            setIsFavorite()
        }
    }


    private fun setDataDetail(userModel: UserModel?) {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        detailViewModel.setDataUser(userModel?.username)
        detailViewModel.getDetailUser().observe(this, { dataUser ->
            showLoading(false)
            if (dataUser != null) {
                tv_name.text = dataUser.name
                tv_company.text = if (dataUser.company == "null") "-" else dataUser.company
                tv_location.text = if (dataUser.location == "null") "-" else dataUser.location
                tv_repo.text = dataUser.repository.toString()
                tv_followers.text = dataUser.followers.toString()
                tv_following.text = dataUser.following.toString()
                tv_bio.text = if (dataUser.bio == "null") "Github User" else dataUser.bio
                Glide.with(this)
                    .load(dataUser.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_avatar)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //set is favorite kalau data dari detailActivity
    private fun setIsFavorite() {
        val cursor = favUserHelper.queryAll()
        val listData = MappingHelper.mapCursorToArrayList(cursor)
        Log.d("DetailActivity", "PELAKU UTAMA: ${user?.username}")
        for (data in listData) {
            Log.d("DetailActivity", "Tersangka : $data")
            if (user?.username == data.username) {
                isFavorite = true
                break
            } else {
                isFavorite = false
            }
        }
        setIconFavorite()
    }

    //set is favorite kalau data favUserActivity
//    private fun setIsFavorite2() {
//        user = intent.getParcelableExtra(EXTRA_FAVORITE)
//        if (user != null) {
//            position = intent.getIntExtra(EXTRA_POSITION, 0)
//            isFavorite = true
//        } else {
//            user = UserModel()
//        }
//        setIconFavorite()
//    }

    private fun setIconFavorite() {
        if (isFavorite) {
            fab_favorite.setImageResource(R.drawable.ic_fav_fill_white_24dp)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_fav_border_white_24dp)
        }
    }

    private fun setFollowersFollowing(userModel: UserModel?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = userModel?.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

