package com.example.cookbook.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.interfaces.ClickInterface
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import com.example.cookbook.databinding.DishItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class RecipeAdapter(options: FirestoreRecyclerOptions<Recipe>, var clickInterface: ClickInterface) :
    FirestoreRecyclerAdapter<Recipe, RecipeAdapter.MyViewHolder>(options) {
private val db by lazy{
    FirebaseFirestore.getInstance()
}
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
              binding.addBtn.setOnClickListener {
                  val recipe=snapshots.get(adapterPosition).decription
                  val time=snapshots.get(adapterPosition).time
                  val id=snapshots.get(adapterPosition).id
                  val dishTitle=snapshots.get(adapterPosition).dishName
                  val creatorId=snapshots.get(adapterPosition).creatorId
                  val cuisine=snapshots.get(adapterPosition).cuisine
                  clickInterface.addToFavorites(recipe,dishTitle,time,id,creatorId,cuisine)
              }
          }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Recipe) {
        val binding=DishItemBinding.bind(holder.view)
        binding.dishTitle.text=model.dishName
        db.collection("favorites").document(model.id).addSnapshotListener{v,e->
            if(v?.exists()!!) {
                binding.addBtn.text="Remove"
                binding.addBtn.setBackgroundColor(Color.RED)
            }
            else{
                binding.addBtn.text="Add"
                binding.addBtn.setBackgroundColor(Color.GRAY)
            }

        }
        Glide.with(holder.view).load(R.drawable.american).into(binding.dishImage)
    }
}