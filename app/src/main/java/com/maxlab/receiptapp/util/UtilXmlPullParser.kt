package com.maxlab.receiptapp.util

import com.maxlab.receiptapp.model.RecipeType
import org.xmlpull.v1.XmlPullParser;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

import java.io.InputStream;
import java.util.ArrayList;


class UtilXmlPullParser {


    private var recipeTypes : ArrayList<RecipeType>? = null

    private var recipeType : RecipeType = RecipeType();

    private var text :String = "";


    constructor(){
        recipeTypes = ArrayList()
    }


    /**
     * Parse XML Data
     * @param inputStream xml data
     *
     */
    fun  parse(inputStream : InputStream) : ArrayList<RecipeType>? {

        var factory : XmlPullParserFactory

        var  parser : XmlPullParser
        try {

            factory = XmlPullParserFactory.newInstance()
            factory.setNamespaceAware(true)
            parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.getEventType()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                var tagname = parser.name
                when (eventType){
                    XmlPullParser.START_TAG -> {
                        if (tagname.equals("recipetype", ignoreCase = true)) {
                            recipeType =  RecipeType() } }
                    XmlPullParser.TEXT -> {
                        text = parser.getText() }
                    XmlPullParser.END_TAG -> {
                        if (tagname.equals("recipetype",ignoreCase = true)) {
                            // Add recipeType obj to List recipeTypes
                            recipeTypes?.add(recipeType);
                        } else if (tagname.equals("name",ignoreCase = true)) {
                            recipeType.setName(text)
                        } }
                }
                // Process Event until the event is END_DOCUMENT
                eventType = parser.next()
            }
        } catch ( e : XmlPullParserException) {
            e.printStackTrace()
        } catch ( e : IOException) {
           e.printStackTrace()
        }
        // Return List recipeTypes
        return recipeTypes
    }
}