package com.example.lcom151_two.veroapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/register")
    Call<responseModel> userRegister(@Field("userId") String userId,
                             @Field("email") String email,
                             @Field("displayName") String displayName);

    @FormUrlEncoded
    @POST("api/login")
    Call<responseModel> userLogin(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/verify")
    Call<responseModel> verifyUser(@Field("otp") int otp,
                                   @Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/posts")
    Call<responseModel> postComment(@Field("postType") String postType,
                                    @Field("postText") String postText,
                                    @Field("postUrl") String postUrl,
                                    @Field("postCaption") String postCaption,
                                    @Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/likes")
    Call<responseModel> likePost(@Field("postId") int postId,
                                 @Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/follow")
    Call<responseModel> followUser(@Field("userId") String userId,
                                   @Field("fuserId") String fuserId);

    @FormUrlEncoded
    @POST("api/posts/private")
    Call<getPostsResponseModel> getPosts(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("api/profile")
    Call<userProfileResponse> setProfile(@Field("userId") String userId,
               @Field("email") String email,
               @Field("displayName") String displayName);
}
