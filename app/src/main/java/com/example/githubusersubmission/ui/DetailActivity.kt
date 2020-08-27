package com.example.githubusersubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusersubmission.R
import com.example.githubusersubmission.adapter.SectionsPagerAdapter
import com.example.githubusersubmission.model.UserModel
import com.example.githubusersubmission.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        val user = intent.getParcelableExtra<UserModel>(EXTRA_STATE)

        supportActionBar?.title = user?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDataDetail(user)
        setFollowersFollowing(user)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDataDetail(userModel: UserModel?) {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        detailViewModel.setDataUser(userModel?.username)
        detailViewModel.getDetailUser().observe(this, { dataUser ->
            showLoading(false)
            if (dataUser != null) {
                tv_name.text = dataUser.username
                tv_name.text = dataUser.name
                tv_company.text = if (dataUser.company == "null") "-" else dataUser.company
                tv_location.text = if (dataUser.location == "null") "-" else dataUser.location
                tv_repository.text = dataUser.repository.toString()
                tv_bio.text = if (dataUser.bio == "null") "Github User" else dataUser.bio
                Glide.with(this)
                    .load(dataUser.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_avatar)

            }
        })
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
}

