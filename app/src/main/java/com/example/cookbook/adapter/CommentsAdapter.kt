package com.example.cookbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.models.Comment
import com.example.cookbook.R
import com.example.cookbook.databinding.CommentsItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CommentsAdapter(options: FirestoreRecyclerOptions<Comment>) :
    FirestoreRecyclerAdapter<Comment, CommentsAdapter.CommentsViewHolder>(options) {
    inner class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.comments_item,parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int, model: Comment) {
        val binding=CommentsItemBinding.bind(holder.itemView)
        binding.commentTextView.text=model.comment
        binding.userName.text=model.name
        Glide.with(holder.itemView).load(model.photoUrl).into(binding.userProfile)
    }

}