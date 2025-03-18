package com.example.mealscape;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HuggingFaceApiService {
    @POST("v1/completions")
    @Headers({
            "Authorization: Bearer 9ae5150161cd45649ec8753ebcf41c76",
            "Content-Type: application/json"
    })
    Call<MealResponse> getResponse(@Body RequestBody body);

    class RequestBody {
        public String inputs;

        public RequestBody(String inputs) {
            this.inputs = inputs;
        }

        public String getInputs() {
            return inputs;
        }
    }

    class MealResponse {
        private String[] generated_text;

        public String getGeneratedText() {
            return generated_text != null && generated_text.length > 0 ? generated_text[0] : "";
        }
    }
}



