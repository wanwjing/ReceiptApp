package com.maxlab.receiptapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.maxlab.receiptapp.model.RecipeBean
import com.maxlab.receiptapp.component.TopBotField
import com.maxlab.receiptapp.util.UtilStorage
import kotlinx.android.synthetic.main.activity_add.*
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import com.maxlab.receiptapp.model.RecipeType
import android.widget.ArrayAdapter
import java.util.*
import kotlin.collections.ArrayList


class AddActivity : BaseActivity() {

    private var recipeBean : RecipeBean = RecipeBean()
    private var recipeTypes : ArrayList<RecipeType>? = null
    companion object {
        val request_tag = 0x1000
        val request_data = "recipe data bean"
        val pick_img_data = 0x1010
    }

    override val uiContentId: Int
        get() = R.layout.activity_add

    override fun uiData() {
        recipeTypes = getRecipeTypeData()
    }


    override fun uiFlow(context: Context) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tbf_title.getEtDescription().addTextChangedListener(object : TopBotField.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recipeBean.setName(p0.toString())
            }

        })

        val adapter = ArrayAdapter<RecipeType>(this, R.layout.item_type, recipeTypes)
        sprType.adapter = adapter
        sprType.setSelection(0)
        sprType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                recipeBean.setType(recipeTypes?.get(p2)?.getName().toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }


        tbf_desc.getEtDescription().addTextChangedListener(object : TopBotField.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recipeBean.setDescription(p0.toString())
            }

        })
        tbf_ingredient.getEtDescription().addTextChangedListener(object : TopBotField.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recipeBean.setIngredient(p0.toString())
            }

        })
        tbf_step.getEtDescription().addTextChangedListener(object : TopBotField.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recipeBean.setStep(p0.toString())
            }
        })

        civUpload.setOnClickListener({
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), pick_img_data)})

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == pick_img_data){
                val selectedImage = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

                if(bitmap != null){
                    UtilStorage(baseContext,object : UtilStorage.StorageListener{
                        override fun onSuccess(path: String) {
                            // set new path
                            recipeBean.setImgPath(path)
                            civUpload.setImageBitmap(bitmap)
                        }
                        override fun onFail(message: String) {
                            // do nothing
                        }
                    }).saveToInternalStorage(bitmap)

                }


            }

        }

    }

    private fun backToMainActivity(){
        recipeBean.setId(System.currentTimeMillis().toString())
        val returnIntent = Intent()
        returnIntent.putExtra(request_data,recipeBean)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_add, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.i_save -> backToMainActivity()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}