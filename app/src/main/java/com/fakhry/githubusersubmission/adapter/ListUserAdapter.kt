package com.fakhry.githubusersubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.model.UserModel
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listUser = ArrayList<UserModel>()

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }

    fun setData(items: ArrayList<UserModel>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
            .error(R.drawable.ic_broken_image_24dp)
            .into(holder.ivAvatar)
        holder.tvIdNumber.text = user.idNumber.toString()
        holder.tvUrl.text = user.userUrl
        holder.tvName.text = user.username
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[position]) }
    }

    override fun getItemCount(): Int = listUser.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAvatar: ImageView = itemView.iv_avatar
        var tvName: TextView = itemView.tv_name
        var tvUrl: TextView = itemView.tv_url
        var tvIdNumber: TextView = itemView.tv_id_number
    }
}