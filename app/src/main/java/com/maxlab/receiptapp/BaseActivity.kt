package com.maxlab.receiptapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_base.*
import android.widget.ArrayAdapter
import androidx.core.view.MenuItemCompat
import android.widget.Spinner
import com.maxlab.receiptapp.model.RecipeType
import com.maxlab.receiptapp.util.UtilXmlPullParser
import java.io.IOException


abstract class BaseActivity : AppCompatActivity() {

    // set XML Id here
    abstract val uiContentId : Int;

    abstract fun uiData()
    // set uiFlow create Action here
    abstract fun uiFlow(context: Context)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setupToolBar()



    val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(uiContentId, null)
        view.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        llContent.addView(view)

        uiData()
        uiFlow(this);

    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        return super.navigateUpTo(upIntent)
    }

    fun setupToolBar(name: String = getString(R.string.app_name)){

        supportActionBar?.title = name

    }
    protected fun getRecipeTypeData() : ArrayList<RecipeType> {
        var recipeTypes : ArrayList<RecipeType>? = null
        // Extract Recipe Types from XML
        try {
            val parser = UtilXmlPullParser()
            recipeTypes = parser.parse(assets.open("recipetypes.xml"))

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return recipeTypes!!
    }

}
