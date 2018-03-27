package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.apiClasses.UpdateDeletePostResponse;
import com.example.lcom151_two.veroapp.fragments.PostsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePost extends AppCompatActivity {

    EditText postContent;
    Button update;
    String content,type;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        initView();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost();
            }
        });
    }

    public void initView()
    {
        postContent=(EditText)findViewById(R.id.postContent);
        update=(Button)findViewById(R.id.updatePost);

        content=getIntent().getStringExtra("content");
        type=getIntent().getStringExtra("postType");
        id=getIntent().getIntExtra("postId",0);

        postContent.setText(content);
    }

    public void updatePost()
    {
        String newContent=postContent.getText().toString().trim();
        if(type.equals("text"))
        {
            if(TextUtils.isEmpty(newContent)){
                newContent=content;
            }

        }

        //Toast.makeText(this, newContent, Toast.LENGTH_SHORT).show();

        Call<UpdateDeletePostResponse> call=GlobalClass.apiInterface.updatePost(newContent,id);
        call.enqueue(new Callback<UpdateDeletePostResponse>() {
            @Override
            public void onResponse(Call<UpdateDeletePostResponse> call, Response<UpdateDeletePostResponse> response) {
                if(response.code()==200){
                    Toast.makeText(UpdatePost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UpdatePost.this, UserHome.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UpdatePost.this, "Problem updating post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateDeletePostResponse> call, Throwable t) {
                Toast.makeText(UpdatePost.this, "Failure occurred "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        Intent intent=new Intent(UpdatePost.this, UserHome.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(UpdatePost.this, UserHome.class);
        startActivity(intent);
        finish();
    }
}
