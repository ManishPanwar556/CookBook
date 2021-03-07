package com.example.cookbook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.adapter.RecipeAdapter
import com.example.cookbook.databinding.ActivityFavoritesBinding
import com.example.cookbook.interfaces.ClickInterface
import com.example.cookbook.models.Recipe
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity(), ClickInterface {
    lateinit var binding: ActivityFavoritesBinding
    lateinit var adapter: RecipeAdapter
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setTitle("Favorites")
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val query = db.collection("favorites")
        val options =
            FirestoreRecyclerOptions.Builder<Recipe>().setQuery(query, Recipe::class.java).build()
        adapter=RecipeAdapter(options, this)
        binding.favoritesRev.adapter = adapter
        binding.favoritesRev.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(
        recipe: String,
        dishTitle: String,
        time: String,
        id: String,
        creatorId: String
    ) {
        val intent = Intent(this, DescriptionActivity::class.java)
        intent.putExtra("recipe", recipe)
        intent.putExtra("time", time)
        intent.putExtra("id", id)
        intent.putExtra("dishTitle", dishTitle)
        intent.putExtra("creatorId", creatorId)
        startActivity(intent)
        finish()
    }

    override fun addToFavorites(
        recipe: String,
        dishTitle: String,
        time: String,
        id: String,
        creatorId: String,
        cuisine: String
    ): Boolean {
        var status: Boolean = false
        db.collection("favorites").document(id).get().addOnSuccessListener {
            if (it.exists()) {
                status = false
                removeFromFavorites(id)
            } else {
                status = true
                addRecipe(recipe, dishTitle, time, id, creatorId, cuisine)
            }
        }
        return status

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
    private fun removeFromFavorites(id: String) {
        db.collection("favorites").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Removed From Favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRecipe(
        recipe: String,
        dishTitle: String,
        time: String,
        id: String,
        creatorId: String,
        cuisine: String
    ) {
        val recipe = Recipe(
            dishName = dishTitle,
            cuisine = cuisine,
            time = time,
            id = id,
            decription = recipe,
            creatorId = creatorId
        )
        db.collection("favorites").document(id).set(recipe).addOnSuccessListener {
            Toast.makeText(this, "Added To Favorites", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
        }
    }
}