package com.example.cookbook

data class User(
    val name:String?,
    val url:String?,
    val favoriteRecipes:List<String>,
    val favoriteDish:List<String>
)