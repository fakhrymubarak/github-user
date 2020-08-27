package com.example.githubusersubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.githubusersubmission.R
import com.example.githubusersubmission.adapter.FollowersAdapter
import com.example.githubusersubmission.adapter.FollowingAdapter
import com.example.githubusersubmission.adapter.ListUserAdapter
import com.example.githubusersubmission.model.UserModel
import com.example.githubusersubmission.viewmodel.FollowersViewModel
import com.example.githubusersubmission.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_follows.*

class FollowFragment : Fragment() {
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var followingAdapter: FollowingAdapter

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        private const val ARG_USERNAME = "username"
        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int, username: String?): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putInt(ARG_SECTION_NUMBER, index)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)

        val itemDecor = DividerItemDecoration(context, VERTICAL)
        rv_follows.addItemDecoration(itemDecor)

        rv_follows.setHasFixedSize(true)
        rv_follows.layoutManager = LinearLayoutManager(activity)

        if (arguments != null) {
            when (arguments?.getInt(ARG_SECTION_NUMBER, 0) ?: 1) {
                1 -> {
                    followersAdapter = FollowersAdapter()
                    rv_follows.adapter = followersAdapter

                    followersViewModel = ViewModelProvider(
                        this,
                        ViewModelProvider.NewInstanceFactory()
                    ).get(FollowersViewModel::class.java)
                    followersViewModel.setListFollowers(username.toString())
                    followersViewModel.getUsers().observe(viewLifecycleOwner, { userModel ->
                        if (userModel != null) {
                            followersAdapter.setData(userModel)
                        }
                    })
                    followersAdapter.setOnItemClickCallback(object :
                        ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: UserModel) {
                            showSelectedUser(data)
                        }
                    })
                }
                2 -> {
                    followingAdapter = FollowingAdapter()
                    rv_follows.adapter = followingAdapter

                    followingViewModel = ViewModelProvider(
                        this,
                        ViewModelProvider.NewInstanceFactory()
                    ).get(FollowingViewModel::class.java)
                    followingViewModel.setListFollowing(username.toString())
                    followingViewModel.getUsers().observe(viewLifecycleOwner, { userModel ->
                        if (userModel != null) {
                            followingAdapter.setData(userModel)
                        }
                    })
                    followingAdapter.setOnItemClickCallback(object :
                        ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: UserModel) {
                            showSelectedUser(data)
                        }
                    })
                }
            }
        }
    }

    private fun showSelectedUser(userModel: UserModel) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STATE, userModel)
        startActivity(intent)
    }
}