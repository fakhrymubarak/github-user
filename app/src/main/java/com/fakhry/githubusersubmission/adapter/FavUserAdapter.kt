package com.fakhry.githubusersubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.githubusersubmission.R
import com.fakhry.githubusersubmission.model.FavUserModel
import kotlinx.android.synthetic.main.item_row_user.view.*

class FavUserAdapter : RecyclerView.Adapter<FavUserAdapter.FavUserViewHolder>() {
    var listFavUser = ArrayList<FavUserModel>()
        set(listFavUser) {
            if (listFavUser.size > 0) {
                this.listFavUser.clear()
            }
            this.listFavUser.addAll(listFavUser)
            notifyDataSetChanged()
        }

    fun addItem(favUser: FavUserModel) {
        this.listFavUser.add(favUser)
        notifyItemInserted(this.listFavUser.size - 1)
    }

//    fun updateItem(position: Int, note: FavUserModel) {
//        this.listFavUser[position] = note
//        notifyItemChanged(position, note)
//    }

    fun removeItem(position: Int) {
        this.listFavUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavUser.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        holder.bind(listFavUser[position])
    }

    override fun getItemCount(): Int = this.listFavUser.size

    inner class FavUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: FavUserModel) {
            with(itemView) {
                tv_name.text = favUser.username
                tv_url.text = favUser.userUrl
                tv_id_number.text = favUser.idNumber.toString()
                Glide.with(itemView.context)
                    .load(favUser.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_24dp))
                    .error(R.drawable.ic_broken_image_24dp)
                    .into(iv_avatar)
            }
        }
    }
}
