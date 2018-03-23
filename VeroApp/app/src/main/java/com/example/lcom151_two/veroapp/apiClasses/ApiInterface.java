package com.example.lcom151_two.veroapp.apiClasses;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/login")
    Call<responseModel> userLogin(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/verify")
    Call<responseModel> verifyUser(@Field("otp") int otp,
                                   @Field("userId") String userId);

    @Multipart
    @POST("api/posts")
    Call<getPostsResponseModel> postWithImage(@PartMap HashMap<String,RequestBody> requestBodyHashMap,@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/posts")
    Call<getPostsResponseModel> post(@Field("postType") String postType,
                             @Field("postText") String postText,
                             @Field("userId") String userId,
                             @Field("privacy") Integer privacy);

    @FormUrlEncoded
    @POST("api/likes")
    Call<postLikeResponseModel> likePost(@Field("postId") int postId,
                                         @Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/follow")
    Call<responseModel> followUser(@Field("userId") String userId,
                                   @Field("fuserId") String fuserId);

    @GET("api/post/{userId}")
    Call<getPostsResponseModel> getPosts(@Path("userId") String userId);

    @Multipart
    @POST("api/profile")
    Call<userProfileResponse> setProfile(@PartMap HashMap<String, RequestBody> requestBodyHashMap, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/profile")
    Call<userProfileResponse> profileWithoutImage(@Field("userId") String userId,
                                                  @Field("email") String email,
                                                  @Field("displayName") String displayName,
                                                  @Field("userStatus") String userStatus,
                                                  @Field("userName") String userName);

    @FormUrlEncoded
    @POST("api/following")
    Call<FollowingUserResponseModel> following(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/postsLiked")
    Call<PostsLikedResponseModel> postsLiked(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/likes/post")
    Call<UserLikedPost> username(@Field("postId") Integer postId);

    @FormUrlEncoded
    @POST("api/comments/post")
    Call<UserCommentPost> comments(@Field("postId") Integer postId);

    @FormUrlEncoded
    @POST("api/comments")
    Call<CommentResponseModel> postComment(@Field("commentText") String commentText,
                                           @Field("userId") String userId,
                                           @Field("postId") int postId);

    @GET("api/search/{query}")
    Call<SearchUserResponseModel> searchUser(@Path("query") String query);

}
