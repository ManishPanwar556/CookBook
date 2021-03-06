package com.example.cookbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.adapter.RecipeAdapter
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RecipeListActivity : AppCompatActivity(),ClickInterface {
    lateinit var cuisine: String
    lateinit var binding: FragmentRecipeListBinding
    val auth = FirebaseAuth.getInstance()
    val db by lazy {
        FirebaseFirestore.getInstance()
    }
lateinit var adapter: RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cuisine = intent.extras?.getString("cuisine").toString()
        val query=db.collection("recipes").whereEqualTo("cuisine",cuisine)
        val options=FirestoreRecyclerOptions.Builder<Recipe>().setQuery(query,Recipe::class.java).build()
        adapter= RecipeAdapter(options,this)
        binding.rev.adapter=adapter
        binding.rev.layoutManager=LinearLayoutManager(this)
        controlVisibility()
        query.get().addOnSuccessListener {
            Log.e("query","${it.documents.size}")
        }
        binding.addRecipe.setOnClickListener {
            val intent = Intent(this, RecipeDetailsActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }
    private fun controlVisibility() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            binding.addRecipe.visibility = View.VISIBLE
        } else {
            binding.addRecipe.visibility = View.GONE
        }
    }

    override fun onClick(recipe: String, dishTitle: String, time: String, id: String,creatorId:String) {
        val intent=Intent(this,DescriptionActivity::class.java)
        intent.putExtra("recipe",recipe)
        intent.putExtra("time",time)
        intent.putExtra("id",id)
        intent.putExtra("dishTitle",dishTitle)
        intent.putExtra("creatorId",creatorId)
        startActivity(intent)
    }
}
