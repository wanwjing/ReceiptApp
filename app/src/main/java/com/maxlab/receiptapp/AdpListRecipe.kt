package com.maxlab.receiptapp;

import android.content.Context
import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.maxlab.receiptapp.model.RecipeBean
import kotlinx.android.synthetic.main.item_recipe.view.*
import java.io.File


class AdpListRecipe(val context: Context, val items : ArrayList<RecipeBean>, val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var file = File(items.get(position).getImgPath())
        if(file.exists()) holder?.iv_food?.setImageURI(file.toUri())

        holder?.tvFoodTitle?.text = items.get(position).getName()
        holder?.tvFoodDesc?.text = items.get(position).getDescription()

        holder.itemView.setOnClickListener( {
            onItemClickListener.onPress(position,items.get(position))
        })
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val iv_food = view.iv_food
    val tvFoodTitle = view.tvFoodTitle
    val tvFoodDesc = view.tvFoodDesc

}

interface OnItemClickListener{
    fun onPress( no : Int, item : RecipeBean)
}
