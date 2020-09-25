package com.fakhry.githubusersubmission.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.adapter.ListUserAdapter
import com.fakhry.githubusersubmission.model.UserModel
import com.fakhry.githubusersubmission.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecyclerView()
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        mainViewModel.getUsers().observe(this@MainActivity, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (isOnline(this@MainActivity)) {
                    if (query.isNotEmpty()) {
                        showLoading(true)
                        mainViewModel.setListUser(query)
                        iv_search_user.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@MainActivity, R.string.you_offline, Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.activity_favorite_user -> {
                val intent = Intent(this, FavUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        rv_user.setHasFixedSize(true)
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(userModel: UserModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STATE, userModel)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
