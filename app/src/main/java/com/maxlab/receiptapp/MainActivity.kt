package com.maxlab.receiptapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxlab.receiptapp.model.RecipeBean
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxlab.receiptapp.util.UtilSharedPreferences
import com.maxlab.receiptapp.util.UtilStorage
import java.io.IOException
import java.io.InputStream


class MainActivity : BaseActivity() {

    private var recipes : ArrayList<RecipeBean> =  ArrayList<RecipeBean>()
    private val cacheTag : String = "recipe bean cache"

    override val uiContentId: Int
        get() = R.layout.activity_main

    override fun uiData() {
        firstTimeCacheData()
    }

    override fun uiFlow(context: Context) {

        // set recycler view adapter
        rv_content.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rv_content.adapter =
            AdpListRecipe(this, recipes, object : OnItemClickListener {
                override fun onPress(num: Int, recipetbean: RecipeBean) {
                    var intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.request_data,recipetbean)
                    startActivityForResult(intent,DetailActivity.request_tag)
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK) {
                if(requestCode == AddActivity.request_tag){
                    val recipeBean = data?.getSerializableExtra(AddActivity.request_data) as RecipeBean
                    recipes.add(recipeBean)
                    rv_content.adapter?.notifyDataSetChanged()
                    storeDataFromLocal()
                }else if(requestCode == DetailActivity.request_tag){
                    val recipeBean = data?.getSerializableExtra(DetailActivity.request_data) as RecipeBean
                    recipes.forEachIndexed{ index, item ->
                        if(recipeBean.getId().equals(item.getId())){
                            recipes.set(index,recipeBean)
                            rv_content.adapter?.notifyDataSetChanged()
                            storeDataFromLocal()
                        }
                    }
                }

            }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_recipe, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.i_add -> navigateToAdd()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToAdd(){
        var intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent,AddActivity.request_tag)
    }

    private fun firstTimeCacheData(){

        if(!UtilSharedPreferences.getInstance(this)!!.isFirstTime()){
            retrieveDataFromLocal()
            return
        }

        if(recipes != null){
            recipes.add(RecipeBean("1","burger.png","burger","delicious burger","ingredient","step 1 , step 2",""))
            recipes.add(RecipeBean("2","fruit.png","fruit","delicious fruit","ingredient","step 1 , step 2","A"))
            recipes.add(RecipeBean("3","pasta.png","paste","delicious paste","ingredient","step 1 , step 2","A"))

            recipes.forEachIndexed { index, recipe ->
                var nameImg = recipe.getImgPath()!!
                var ima = getAssetData(nameImg)
                if (ima != null) {
                    UtilStorage(baseContext,object : UtilStorage.StorageListener{
                        override fun onSuccess(path: String) {
                            // set new path
                            recipes.get(index).setImgPath(path)
                        }
                        override fun onFail(message: String) {
                            // do nothing
                        }
                    }).saveToInternalStorage(ima)
                }
            }

            UtilSharedPreferences.getInstance(this)!!.setIsFirsTime(false)
        }



    }

    private fun getAssetData(fileName: String) : Bitmap?{
        var assetInStream: InputStream? = null
        var bit : Bitmap? = null

        try {
            assetInStream = assets.open(fileName)
            bit = BitmapFactory.decodeStream(assetInStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (assetInStream != null)
                assetInStream!!.close()
            return bit
        }

    }

    private fun retrieveDataFromLocal(){
        val data = UtilSharedPreferences.getInstance(this)?.getData(cacheTag)
        val itemType = object : TypeToken<ArrayList<RecipeBean>>() {}.type
        this.recipes =  Gson().fromJson(data, itemType)
    }

    private fun storeDataFromLocal(){
        UtilSharedPreferences.getInstance(this)?.saveData(cacheTag,Gson().toJson(recipes))
    }




}
