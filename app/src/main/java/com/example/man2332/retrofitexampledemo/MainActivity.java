package com.example.man2332.retrofitexampledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();//.getPosts() defined by retrofit already for us
        //.enqueue calls .getPost() and returns the response in onResponse() callback
        //-this is called on a background thread
        call.enqueue(new Callback<List<Post>>() {//execute the call and get response back
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {//response code is NOT 200-400
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();//returns an array(List<Post>) of Post objects

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
//<uses-permission android:name="android.permission.INTERNET"/>

//-RETROFIT - android/java library for requesting data from web service
//  -its abstraction layer for HTTP calls(which does all the networking code/work for us)
//  -new Retrofit.Builder() - returns a Retrofit object
//  -.baseUrl("https://jsonplaceholder.typicode.com/") - adds a base url used for HTTP requests
//  -.build() - builds the object

//-CONVERTERS - JSON TO JAVA OBJECTS
//  -GSON - implementation 'com.squareup.retrofit2:converter-gson:2.4.0'  - GSON is one of many converters
//  -.addConverterFactory(GsonConverterFactory.create()) - this Retrofit obj will now convert all it's HTTP
//      responses(JSON) to java objects for us
//-ROUTES - paths for getting data, also called endpoints

//-RETROFIT - retrofit.create(INTERFACE CLASS HERE)
//  - provide Retrofit with an interface class
//  -that interface class must have abstract methods with @ANNOTATIONS
//  -Annotations describe what it wants retrofit to do
//  -for example @GET("posts") - tells retrofit it wants that method to perform a GET http request
//      and get from baseURL/posts -where baseURL should have been provided when building the retrofit obj
//      basically GET from https://jsonplaceholder.typicode.com/posts
//-CALL<List<>> - is a interface that helps a retrofit method with it's calls(hhtp requests/response)
//  -

//-build.gradle - depn.

//-retrofit.create(JsonPlaceHolderApi.class);   - retrofit will define the methods for us with .create()
//- cannot call network requests on mainthread Ex. call.execute()

