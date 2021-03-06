package com.example.cookbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.ClickInterface
import com.example.cookbook.R
import com.example.cookbook.Recipe
import com.example.cookbook.databinding.DishItemBinding
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RecipeAdapter(options: FirestoreRecyclerOptions<Recipe>,var clickInterface: ClickInterface) :
    FirestoreRecyclerAdapter<Recipe, RecipeAdapter.MyViewHolder>(options) {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
          init {
              val binding=DishItemBinding.bind(view)
              binding.cardView.setOnClickListener {
                  val recipe=snapshots.get(adapterPosition).decription
                  val time=snapshots.get(adapterPosition).time
                  val id=snapshots.get(adapterPosition).id
                  val dishTitle=snapshots.get(adapterPosition).dishName
                  val creatorId=snapshots.get(adapterPosition).creatorId
                  clickInterface.onClick(recipe,dishTitle,time,id,creatorId)
              }
          }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Recipe) {
        val dishTitle=holder.view.findViewById<TextView>(R.id.dishTitle)
        dishTitle.text = model.dishName
    }
}