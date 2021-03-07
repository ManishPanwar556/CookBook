package com.example.cookbook.interfaces

interface ClickInterface {
    fun onClick(recipe:String,dishTitle:String,time:String,id:String,creatorId:String)
    fun addToFavorites(recipe:String,dishTitle:String,time:String,id:String,creatorId:String,cuisine:String):Boolean
}