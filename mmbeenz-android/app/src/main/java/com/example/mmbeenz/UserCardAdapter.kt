package com.example.mmbeenz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserCardAdapter(private val userData: List<UserItem>) : RecyclerView.Adapter<UserCardAdapter.UserCardView>() {
    class UserCardView(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageV: ImageView = itemView.findViewById(R.id.user_image_view)
        val usernameV: TextView = itemView.findViewById(R.id.user_name_view)
        val ratingV: TextView = itemView.findViewById(R.id.user_rating_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardView {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_activity, parent
            ,false)
        return UserCardView(itemView)
    }

    override fun onBindViewHolder(holder: UserCardView, position: Int) {
        val currentUser = userData[position]
        val host ="http://10.0.2.2:7000"
        Picasso.get().load(host + "/image/${currentUser.username}.png").into(holder.imageV)


        //holder.imageV.setim = currentUser.image
        holder.usernameV.text = currentUser.username
        val ratings = currentUser.rating.toString() + " beenz"
        holder.ratingV.text = ratings
    }

    override fun getItemCount(): Int {
        return userData.size
    }
}