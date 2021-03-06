package com.example.cookbook

data class Recipe(
    var dishName: String="",
    var cuisine: String="",
    val time: String="",
    var decription: String="",
    val id:String="",
    val creatorId:String=""
)