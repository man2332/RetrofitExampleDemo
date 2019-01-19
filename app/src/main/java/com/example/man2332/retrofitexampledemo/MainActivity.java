package com.example.man2332.retrofitexampledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//@PATH @QUERY @QUERYMAP @URL
//@GET @POST
//@PUT @PATCH @DELETE
//OkHttpClient Logging
public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Gson gson = new GsonBuilder().serializeNulls().create();//used to allow null values in gson
        //-then pass in the gson obj into .create() to tell retrofit to use it instead of default gson
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)//tell retrofit to use our custom client instead of a default 1
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getPosts();
        //getComments();
        //createPost();
        updatePost();
        //deletePost();
    }

    private void getComments() {
        Call<List<Comment>> call = jsonPlaceHolderApi
                .getComments(2);
//        Call<List<Comment>> call = jsonPlaceHolderApi
//                .getComments("https://jsonplaceholder.typicode.com/posts/3/comments");
        //https://jsonplaceholder.typicode.com/ will replace the base url(if its a diff url)
        //-.getComments("posts/3/comments"); also works but uses orginal url


        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Comment> comments = response.body();//returns contents of the body

                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getPosts(){
        //u can pass null to .getPost() if u dont want to sort it by anything
        //.getPosts() defined by retrofit already for us
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,4},"id","desc");

        //use a MAP object to provide the paramters to retrofit
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);


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

    private void createPost() {
        //pass our fields using a Post object
//        Post post = new Post(23, "New Title", "New Text");
//        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        //place our fields directly
//        Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New Text");

        //pass our fields using a MAP
        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");

        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void updatePost() {
        Post post = new Post(12, null, "New Text");

        //Call<Post> call = jsonPlaceHolderApi.putPost(5, post);//replace entire object
        Call<Post> call = jsonPlaceHolderApi.patchPost(5, post);
        //-by default gson will not accept null values, if a null value is passed, gson will just
        //  remove the whole field when it sends it as a POST request,
        //  so when it gets posted, the orginal value of the object on the API
        //  stays the same. But u can make it so gson does accept null values by configuring
        //  the gson obj and placing it in the Retrofit builder.(line 32)

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code: " + response.code());//shows 200 if delete was successful
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
//  -.baseUrl("https://jsonplaceholder.typicode.com/") - base urls must end with backslash /
//      -this ensures our base urls and relative paths query correctly(relative path dont have backslash)

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



//-build.gradle -
//    implementation 'com.squareup.retrofit2:retrofit:2.4.0'
//    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'.
//    implementation 'com.squareup.okhttp3:logging-interceptor:3.12.1'

//-retrofit.create(JsonPlaceHolderApi.class);   - retrofit will define the methods for us with .create()
//- cannot call network requests on mainthread Ex. call.execute()

//https://codinginflow.com/tutorials/android/retrofit/part-5-httplogginginterceptor

