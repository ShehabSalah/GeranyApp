package com.shehabsalah.geranyapp.controllers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.model.Category;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.util.Config;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ShehabSalah on 8/10/17.
 * CategoriesController class responsible on the communication between server, data model and views.
 * This class contain the business logic of the application category and its main functionality.
 */

public class CategoriesController {
    private ArrayList<Category> categories;
    private DatabaseReference categoryRef;

    /**
     * Constructor to initiate categories list
     * */
    public CategoriesController() {
        this.categories = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoryRef = database.getReference(Config.DB_CATEGORIES);
    }


    /**
     * This method communicates with Firebase server and fetch all the application categories
     * ArrayList<Category>
     * */
    public void fillCategories(){
        Query query = categoryRef.orderByChild(Config.CATEGORY);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categories = new ArrayList<>();
                for (DataSnapshot placeSnap: snapshot.getChildren()) {
                    Category category = placeSnap.getValue(Category.class);
                    categories.add(0,category);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * This method provide access to the categories list size
     * @return int that contain the categories size
     * */
    public int getCategoriesSize(){
        return categories.size();
    }

    /**
     * This method provide access to the application categories.
     * @return ArrayList<Category>
     * */
    public ArrayList<Category> getCategories(){
        return categories;
    }

    /**
     * This method return category at given position
     * @param position the category position
     * @return Category object that contain the category information at a given position
     * */
    public Category getCategoryAtPosition(int position){
        return categories.get(position);
    }

    /**
     * This method allows to add new category to the application categories.
     * @param categoryName string has the new category name.
     * @return Category object which contain the category information that just added.
     * */
    public Category addNewCategory(String categoryName){
        //ToDo: add category to the database and when the id is returned add category to the list
        Category category = new Category("Category_id","CategoryName");
        categories.add(category);
        return null;
    }

}
