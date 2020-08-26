package com.example.githubusersubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusersubmission.R
import com.example.githubusersubmission.model.UserModel
import kotlinx.android.synthetic.main.item_row_follow.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.ListViewHolder>() {
    private val listFollowers = ArrayList<UserModel>()

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserModel) {
            with(itemView) {
                tv_name.text = user.username
                tv_url.text = user.userUrl
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_avatar)
            }
        }
    }

    fun setData(items: ArrayList<UserModel>) {
        listFollowers.clear()
        listFollowers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_follow, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) =
        holder.bind(listFollowers[position])

    override fun getItemCount(): Int = listFollowers.size
}