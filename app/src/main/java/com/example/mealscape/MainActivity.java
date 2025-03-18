package com.example.mealscape;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MealsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new MealsDatabaseHelper(this);

        EditText dietaryInput = findViewById(R.id.editTextDietary);
        TextView resultView = findViewById(R.id.textViewResult);
        TextView groceryListView = findViewById(R.id.textViewGroceryList);
        Button generateButton = findViewById(R.id.buttonGenerate);
        Button saveButton = findViewById(R.id.buttonSave);
        Button shareButton = findViewById(R.id.buttonShare);
        Button viewFavoritesButton = findViewById(R.id.buttonViewFavorites);

        generateButton.setOnClickListener(v -> {
            String dietaryInfo = dietaryInput.getText().toString().trim();
            if (dietaryInfo.isEmpty()) {
                dietaryInput.setError("Please enter dietary information");
                dietaryInput.requestFocus();
            } else {
                fetchMealPlan(dietaryInfo, resultView, groceryListView);
            }
        });

        saveButton.setOnClickListener(v -> {
            String recipe = resultView.getText().toString().trim();
            if (!recipe.isEmpty()) {
                if (!databaseHelper.isFavoriteExists(recipe)) {
                    databaseHelper.addFavorite("Meal", recipe);
                    Toast.makeText(this, "Meal saved to favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Meal already exists in favorites!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No meal plan to save!", Toast.LENGTH_SHORT).show();
            }
        });

        shareButton.setOnClickListener(v -> {
            String recipe = resultView.getText().toString().trim();
            if (!recipe.isEmpty()) {
                shareRecipe(recipe);
            } else {
                Toast.makeText(this, "No meal plan to share!", Toast.LENGTH_SHORT).show();
            }
        });

        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void fetchMealPlan(String dietaryInfo, TextView resultView, TextView groceryListView) {
        OpenAiApiService apiService = RetrofitClient.getClient().create(OpenAiApiService.class);

        // Create request body with the correct format for AIML API
        OpenAiApiService.OpenAiRequestBody.Message[] messages = new OpenAiApiService.OpenAiRequestBody.Message[] {
                new OpenAiApiService.OpenAiRequestBody.Message("user", "Create a meal recipe based on these dietary preferences: " + dietaryInfo)
        };

        OpenAiApiService.OpenAiRequestBody requestBody = new OpenAiApiService.OpenAiRequestBody(
                "gpt-3.5-turbo", // Model name, adjust if necessary
                messages
        );

        // Make the API call
        Call<OpenAiApiService.OpenAiResponse> call = apiService.getResponse(requestBody);
        call.enqueue(new Callback<OpenAiApiService.OpenAiResponse>() {
            @Override
            public void onResponse(Call<OpenAiApiService.OpenAiResponse> call, Response<OpenAiApiService.OpenAiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().getGeneratedText();
                    Log.d("API Response", "Result: " + result);
                    resultView.setText(result); // Update UI with the result
                } else {
                    Log.e("API Error", "Status Code: " + response.code() + ", Error: " + response.errorBody());
                    resultView.setText("Failed to fetch meal plan.");
                }
            }

            @Override
            public void onFailure(Call<OpenAiApiService.OpenAiResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                resultView.setText("Error: Unable to connect to the API.");
            }
        });
    }

    private void shareRecipe(String recipe) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipe);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share recipe with:"));
    }
}
