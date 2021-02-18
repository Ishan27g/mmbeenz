package com.example.mmbeenz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

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
        holder.imageV.setImageResource(R.drawable.abed)
        holder.usernameV.text = currentUser.username
        val ratings = currentUser.rating.toString() + " beenz"
        holder.ratingV.text = ratings
    }

    override fun getItemCount(): Int {
        return userData.size
    }
}