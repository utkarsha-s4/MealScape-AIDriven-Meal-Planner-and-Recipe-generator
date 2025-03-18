package com.example.mealscape;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private ImageView imageViewBackground;
    public MealsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        databaseHelper = new MealsDatabaseHelper(this);

        TextView textView = findViewById(R.id.textViewTitle);
        textView.setTextColor(getResources().getColor(R.color.primary));



        ListView listView = findViewById(R.id.listViewFavorites);
        List<String> favorites = databaseHelper.getFavorites();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favorites);
        listView.setAdapter(adapter);

    }
}
