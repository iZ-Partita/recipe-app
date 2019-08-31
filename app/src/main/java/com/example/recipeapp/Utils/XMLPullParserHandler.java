package com.example.recipeapp.Utils;

import com.example.recipeapp.pojo.RecipeType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserHandler {

    private List<RecipeType> recipeTypeList = new ArrayList<>();
    private RecipeType recipeType;
    private String text;

    public List<RecipeType> getEmployees() {
        return recipeTypeList;
    }

    public List<RecipeType> parse(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("recipetype")) {
                            // create a new instance of employee
                            recipeType = new RecipeType();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("recipetype")) {
                            // add employee object to list
                            recipeTypeList.add(recipeType);
                        } else if (tagName.equalsIgnoreCase("id")) {
                            recipeType.setId(text);
                        } else if (tagName.equalsIgnoreCase("name")) {
                            recipeType.setName(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipeTypeList;
    }
}
