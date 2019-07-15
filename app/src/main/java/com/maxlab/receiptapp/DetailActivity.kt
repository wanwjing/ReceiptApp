package com.maxlab.receiptapp

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.maxlab.receiptapp.model.RecipeBean
import com.maxlab.receiptapp.component.TopBotField
import kotlinx.android.synthetic.main.activity_add.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.get
import com.maxlab.receiptapp.model.RecipeType
import com.maxlab.receiptapp.util.UtilStorage
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener




class DetailActivity : BaseActivity() {

    private var recipeBean : RecipeBean = RecipeBean()
    private var isEditable : Boolean = false
    private var recipeTypes : ArrayList<RecipeType>? = null

    companion object {
        val request_tag = 0x1001
        val request_data = "recipe data detail bean"
    }

    override val uiContentId: Int
        get() = R.layout.activity_detail

    override fun uiData() {
        recipeBean = intent.getSerializableExtra(request_data) as RecipeBean
        recipeTypes = getRecipeTypeData()
    }


    override fun uiFlow(context: Context) {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tbf_title.getEtDescription().setText(recipeBean.getName())
        tbf_desc.getEtDescription().setText(recipeBean.getDescription())
        val adapter = ArrayAdapter<RecipeType>(this, R.layout.item_type, recipeTypes)
        sprType.adapter = adapter
        sprType.setSelection(searchKeyRecipeType(recipeBean.getType()))

        tbf_ingredient.getEtDescription().setText(recipeBean.getIngredient())
        tbf_step.getEtDescription().setText(recipeBean.getStep())
        if(recipeBean.getImgPath() != null){
            civUpload.setImageURI(Uri.parse(recipeBean.getImgPath()))
        }

        if(isEditable){
            enableEditable()
        }else{
            disableEditable()
        }

    }

    private fun searchKeyRecipeType(data : String) : Int{
        recipeTypes?.forEachIndexed{  index, recipe ->
            if(recipe.getName().equals(data)){
                return index
            }
        }
        return 0
    }

    private fun enableEditable(){

        tbf_title.enableKeyListener()
        tbf_desc.enableKeyListener()
        tbf_ingredient.enableKeyListener()
        tbf_step.enableKeyListener()

        tbf_title.getEtDescription().addTextChangedListener(object : TopBotField.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recipeBean.setName(p0.toString())
            }

        })


        sprType.isClickable = true
        sprType.isEnabled = true
        sprType.onItemSelectedListener = object : OnItemSelectedListener {
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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), AddActivity.pick_img_data)})


    }

    private fun disableEditable(){

        tbf_title.disableKeyListener()
        tbf_desc.disableKeyListener()
        tbf_ingredient.disableKeyListener()
        tbf_step.disableKeyListener()
        civUpload.setOnClickListener(null)
        sprType.onItemSelectedListener = null
        sprType.isClickable = false
        sprType.isEnabled = false
    }

    private fun backToMainActivity(){
        val returnIntent = Intent()
        returnIntent.putExtra(request_data,recipeBean)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == AddActivity.pick_img_data){
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detail, menu)

        val ic_save = resources.getDrawable(R.drawable.ic_save)
        val ic_edit = resources.getDrawable(R.drawable.ic_edit)
        if(isEditable){
            menu.get(0).icon = ic_save
        }else{
            menu.get(0).icon = ic_edit
        }


        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.i_edit -> {
                if(isEditable){
                    backToMainActivity()
                    return super.onOptionsItemSelected(item)
                }
               isEditable = !isEditable
                invalidateOptionsMenu();
                uiFlow(this)

            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}