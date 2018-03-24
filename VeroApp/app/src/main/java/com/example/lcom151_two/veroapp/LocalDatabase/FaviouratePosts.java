package com.example.lcom151_two.veroapp.LocalDatabase;

import com.example.lcom151_two.veroapp.ModalClasses.PostsModelClass;

public class FaviouratePosts {

    int _id;
    String postText,postTime,postPic,userProfile,displayName;
    int postId,comments,likescnt;
    //PostsModelClass postsModelClass;

    public FaviouratePosts(){}

    public FaviouratePosts(int id, String postText,String postTime,String postPic,String userProfile,String displayName,int postId,int comments,int likescnt)
    {
        this._id=id;
        this.postText=postText;
        this.postTime=postTime;
        this.postPic=postPic;
        this.userProfile=userProfile;
        this.displayName=displayName;
        this.postId=postId;
        this.comments=comments;
        this.likescnt=likescnt;
        //this.postsModelClass=postsModelClass;
    }

    public FaviouratePosts(PostsModelClass postsModelClass){
        this.postText=postsModelClass.getPostText();
        this.postTime=postsModelClass.getPostTime();
        this.postPic=postsModelClass.getPostPic();
        this.userProfile=postsModelClass.getUserProfile();
        this.displayName=postsModelClass.getDisplayName();
        this.postId=postsModelClass.getPostId();
        this.comments=postsModelClass.getComments();
        this.likescnt=postsModelClass.getLikescnt();
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setLikescnt(int likescnt) {
        this.likescnt = likescnt;
    }

    public int getLikescnt() {
        return likescnt;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setPostPic(String postPic) {
        this.postPic = postPic;
    }

    public String getPostPic() {
        return postPic;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostText() {
        return postText;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
