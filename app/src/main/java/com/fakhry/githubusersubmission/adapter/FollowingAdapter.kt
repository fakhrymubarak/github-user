package com.fakhry.githubusersubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.model.UserModel
import kotlinx.android.synthetic.main.item_row_follow.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: ListUserAdapter.OnItemClickCallback
    private val listFollowing = ArrayList<UserModel>()

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
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_follow, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollowing[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFollowing[position]) }
    }

    fun setOnItemClickCallback(onItemClickCallback: ListUserAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemCount(): Int = listFollowing.size
}