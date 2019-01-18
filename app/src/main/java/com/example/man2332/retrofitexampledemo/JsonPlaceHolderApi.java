package com.example.man2332.retrofitexampledemo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
//@PATH @QUERY @QUERYMAP @URL
public interface JsonPlaceHolderApi {

//    @GET("posts")//@GET(<relative url>)
//    Call<List<Post>> getPosts();//get all posts
    @GET("posts")
    Call<List<Post>> getPosts(@Query("userId")Integer[] userId,
                              @Query("_sort")String sort,
                              @Query("_order")String order);
    //retrofit will transform the arg into "posts?userId=userId"
    //  -@Query("userId") tells retrofit to look for the query called "userId" inside the url string
    //  -then assign it the value of int userId- the result query will be "posts?userId=userId"
    //  -? the question mark tells HTTP when a query is about to being
    //  -& AND sign tells us another query is about to come along
    //-Call<List<Post>> getPosts(@Query("userId")Integer[] userId,@Query("_sort")String sort, @Query("_order")String order);
    //---above line--get posts only matching userId, and sort it and order it by the Query args passed in
    //-Integer[] allows multiple userId's to be passed in
    //  -for multiple args, u can use varargs- Integer... userId
    //  -or just write @Query("userId") Integer userId many times
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);
    //-@QueryMap - just pass in a Map object for retrofit to use as parameters instead
    //  -the key will be the query key(like @Query("userId")- and value will be the arg(like Integer[] userId)

    //-{} replacement block - id(can be any name)
    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);
    //@Path - retrofit will place whatever the arg(in this case it's int postId) into the replace block
    //      matching the replacement id-and automatically turn it into a string
    //      @GET("/posts/{id}/comments") <--- the {id} will be replaced with postId -->(@Path("id")int postId)
    @GET
    Call<List<Comment>> getComments(@Url String url);
    //************POST******************************************************************************
    //-pass our fields using a Post object
    @POST("posts")
    Call<Post> createPost(@Body Post post);

    //-pass our fields directly using args
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    //-pass our fields using a Map
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);
}
//-we need a interface to represent the API of the web service -JsonPlaceHolder is name of the api we using
//-Call retrofit2 - list of post - list of json objs from the api
//-retrofit later define our methods for us - we just need to provide annotation - to tell retrofit what
//  kind of things we want done with that method

//-BASE URL and RELATIVE URL
//-you can also use the entire url instead of just the relative path
//  -@GET("https://jsonplaceholder.typicode.com/posts") will replace the base url with whats given

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


