package com.example.cookbook.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityRecipeDetailsBinding
import com.example.cookbook.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RecipeDetailsActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    val db by lazy {
        FirebaseFirestore.getInstance()
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    var cuisine: String = ""
    lateinit var binding: ActivityRecipeDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.saveRecipe.setOnClickListener {
            if (binding.recipeDescription.text.isEmpty() && binding.dishTitle.text.isEmpty() && binding.time.text.isEmpty() && cuisine=="") {
                Toast.makeText(this, "Cannot Upload Empty Activity", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Cuisne",cuisine)
                uploadRecipe(
                    binding.recipeDescription.text.toString(),
                    binding.dishTitle.text.toString(),
                    binding.time.text.toString(),
                    cuisine
                )
            }
        }
        binding.cuisineSpinner.onItemSelectedListener=this
    }

    private fun uploadRecipe(desc: String, dishTitle: String, time: String, cuisine: String) {
        val uid = UUID.randomUUID().toString()
        val userId=auth.currentUser?.uid
        if(userId!=null) {
            val recipe = Recipe(dishTitle, cuisine, time, desc, uid, userId)
            db.collection("recipes").document(uid).set(recipe).addOnSuccessListener {
                Toast.makeText(this, "Recipe Posted SuccessFully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failure in Posting", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        cuisine=p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


}