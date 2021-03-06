package com.example.cookbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.adapter.CommentsAdapter
import com.example.cookbook.databinding.ActivityDescriptionBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val db by lazy {
        FirebaseFirestore.getInstance()
    }
    lateinit var adapter: CommentsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Recipe")
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val extras = intent.extras
        val id = extras?.getString("id")
        val creatorId=extras?.getString("creatorId").toString()
        val query = db.collection("comments").document(id!!).collection(creatorId)
        val options =
            FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment::class.java).build()
        adapter= CommentsAdapter(options)
        setupRecyclerView(options)
        binding.dishName.text = extras?.getString("dishTitle").toString()
        binding.recipe.text = extras?.getString("recipe").toString()
        binding.time.text = extras?.getString("time").toString()

        binding.commentPostBtn.setOnClickListener {
            if (id != null&&creatorId!=null) {
                postComment(id,creatorId)
            }
        }
    }

    private fun postComment(id: String,creatorId:String) {

        if (binding.commentEditText.text.isNotEmpty()) {
            val rid=UUID.randomUUID().toString()
            db.collection("users").document(auth.currentUser.uid).get().addOnSuccessListener {
                val name = it.get("name").toString()
                val url = it.get("url").toString()
                val comment = Comment(binding.commentEditText.text.toString(), name, url)
                db.collection("comments").document(id).collection(creatorId).document(rid).set(comment).addOnSuccessListener {
                    Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Post Failure", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun setupRecyclerView(options: FirestoreRecyclerOptions<Comment>) {
        binding.commentsRev.adapter=adapter
        binding.commentsRev.layoutManager=LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        val uid = auth.currentUser?.uid
        if (uid == null) {
            binding.commentLayout.visibility = View.GONE
        } else {
            binding.commentLayout.visibility = View.VISIBLE
        }
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}