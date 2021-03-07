package com.example.cookbook.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.ActivityCuisineBinding

class CuisineFragment : Fragment() {
    lateinit var binding: ActivityCuisineBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityCuisineBinding.inflate(inflater)
        setUpCardView()
        return binding.root

    }

    private fun setUpCardView() {
        val bundle = Bundle()
        binding.americanCuisine.setOnClickListener {
            navigateToRecipeList("American")
        }
        binding.chineseCuisine.setOnClickListener {

            navigateToRecipeList("Chinese")
        }
        binding.indianCuisine.setOnClickListener {

            navigateToRecipeList("Indian")
        }
        binding.italianCuisine.setOnClickListener {

            navigateToRecipeList("Italian")
        }
        binding.spanishCuisine.setOnClickListener {

            navigateToRecipeList("Spanish")
        }
        binding.mexicanCuisine.setOnClickListener {

            navigateToRecipeList("Mexican")
        }
    }

    private fun navigateToRecipeList(cuisine:String) {

        val intent=Intent(requireContext(), RecipeListActivity::class.java)
        intent.putExtra("cuisine",cuisine)
        startActivity(intent)
    }
}