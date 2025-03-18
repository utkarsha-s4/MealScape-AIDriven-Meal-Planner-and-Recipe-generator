package com.example.mealscape;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAiApiService {

    @POST("chat/completions") // Remove the leading slash here
    @Headers({
            "Authorization: Bearer 8e1f56e6dbc14c668edac57ddabe56db",  // Replace with your OpenAI API key
            "Content-Type: application/json"
    })
    Call<OpenAiResponse> getResponse(@Body OpenAiRequestBody body);

    // Request Body Structure
    class OpenAiRequestBody {
        private String model;
        private Message[] messages;

        public OpenAiRequestBody(String model, Message[] messages) {
            this.model = model;
            this.messages = messages;
        }

        public String getModel() {
            return model;
        }

        public Message[] getMessages() {
            return messages;
        }

        // Nested class to represent the message structure
        public static class Message {
            private String role;
            private String content;

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }

            public String getRole() {
                return role;
            }

            public String getContent() {
                return content;
            }
        }
    }

    // Response Structure
    class OpenAiResponse {
        private Choice[] choices;

        public String getGeneratedText() {
            return choices != null && choices.length > 0 ? choices[0].message.content.trim() : "";
        }

        static class Choice {
            private Message message;

            public Message getMessage() {
                return message;
            }

            static class Message {
                private String content;

                public String getContent() {
                    return content;
                }
            }
        }
    }
}
