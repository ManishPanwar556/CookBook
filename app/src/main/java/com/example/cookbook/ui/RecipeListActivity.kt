package com.example.cookbook.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapter.RecipeAdapter
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.example.cookbook.interfaces.ClickInterface
import com.example.cookbook.models.Recipe
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RecipeListActivity : AppCompatActivity(), ClickInterface {
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
        val query = db.collection("recipes").whereEqualTo("cuisine", cuisine)
        val options =
            FirestoreRecyclerOptions.Builder<Recipe>().setQuery(query, Recipe::class.java).build()
        updateRecyclerView(options)
        controlVisibility()
        query.get().addOnSuccessListener {
            Log.e("query", "${it.documents.size}")
        }
        binding.addRecipe.setOnClickListener {
            val intent = Intent(this, RecipeDetailsActivity::class.java)
            startActivity(intent)

        }

    }

    private fun updateRecyclerView(options: FirestoreRecyclerOptions<Recipe>) {
        binding.progressBar.visibility = View.GONE
        adapter = RecipeAdapter(options, this)
        binding.rev.adapter = adapter
        binding.rev.layoutManager = LinearLayoutManager(this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorites ->{
                val intent=Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_item, menu)
        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchRecipeCuisine(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    private fun searchRecipeCuisine(query: String?) {
        binding.progressBar.visibility = View.VISIBLE
        query?.let {
            val query1 = db.collection("recipes").whereGreaterThanOrEqualTo("dishName", query)
            query1.get().addOnSuccessListener {
                if (it.documents.isEmpty()) {
                    Toast.makeText(this, "Recipe Not Found", Toast.LENGTH_SHORT).show()
                }
            }
            val options =
                FirestoreRecyclerOptions.Builder<Recipe>().setQuery(query1, Recipe::class.java)
                    .build()
            adapter.updateOptions(options)
            binding.progressBar.visibility = View.GONE
        }

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
