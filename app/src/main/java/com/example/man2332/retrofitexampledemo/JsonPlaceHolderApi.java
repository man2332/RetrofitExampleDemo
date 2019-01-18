package com.example.man2332.retrofitexampledemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts")//@GET(<relative url>)
    Call<List<Post>> getPosts();
}
//-we need a interface to represent the API of the web service -JsonPlaceHolder is name of the api we using
//-Call retrofit2 - list of post - list of json objs from the api
//-retrofit later define our methods for us - we just need to provide annotation - to tell retrofit what
//  kind of things we want done with that method

//-BASE URL and RELATIVE URL

//-SAMPLE OF JSON - https://jsonplaceholder.typicode.com/posts
//  -the base url here is https://jsonplaceholder.typicode.com/
//  -the relative url here is posts
//{
//    "userId": 1,
//    "id": 1,
//    "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
//    "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
//  },
//  {
//    "userId": 1,
//    "id": 2,
//    "title": "qui est esse",
//    "body": "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"
//  },