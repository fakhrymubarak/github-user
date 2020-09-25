package com.fakhry.githubconsumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.githubconsumerapp.R
import com.fakhry.githubconsumerapp.model.UserModel
import kotlinx.android.synthetic.main.item_row_user.view.*

class FavUserAdapter : RecyclerView.Adapter<FavUserAdapter.FavUserViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }

    var listFavUser = ArrayList<UserModel>()
        set(listFavUser) {
            if (listFavUser.size > 0) {
                this.listFavUser.clear()
            }
            this.listFavUser.addAll(listFavUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        holder.bind(listFavUser[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFavUser[position]) }
    }

    override fun getItemCount(): Int = this.listFavUser.size

    inner class FavUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: UserModel) {
            with(itemView) {
                tv_id_number.text = favUser.username
                tv_name.text = favUser.name
                tv_url.text = favUser.userUrl
                Glide.with(itemView.context)
                    .load(favUser.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_avatar)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}
