package com.example.lcom151_two.veroapp.ModalClasses;

public class PostsModelClass {

    String postText,postTime,postPic,userProfile,displayName;
    int postId,comments,likescnt;

    public PostsModelClass(){

    }

    public PostsModelClass(String postText,String postTime,String postPic,String userProfile,String displayName,int comments,int likescnt,int postId){
        this.postText=postText;
        this.postTime=postTime;
        this.postPic=postPic;
        this.userProfile=userProfile;
        this.displayName=displayName;
        this.comments=comments;
        this.likescnt=likescnt;
        this.postId=postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostPic() {
        return postPic;
    }

    public void setPostPic(String postPic) {
        this.postPic = postPic;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikescnt() {
        return likescnt;
    }

    public void setLikescnt(int likescnt) {
        this.likescnt = likescnt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
