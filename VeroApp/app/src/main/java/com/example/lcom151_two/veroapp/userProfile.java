package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class userProfile extends BaseClass {

    ImageView imageView,userprofile;
    EditText name,email;
    Button setprofile,selectpic;
    int PICK_IMAGE_REQUEST = 111;
    String uuserId,userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if(!sp.contains("userId")){
            Intent intent=new Intent(userProfile.this,Login.class);
            startActivity(intent);
            finish();
        }
        else if(sp.contains("profileSet")){
            Intent intent=new Intent(userProfile.this,UserHome.class);
            startActivity(intent);
            finish();
        }
        else {
            uuserId=sp.getString("userId","");
            Toast.makeText(this, "userId : "+uuserId, Toast.LENGTH_SHORT).show();

            imageView=(ImageView)findViewById(R.id.background);
            userprofile=(ImageView)findViewById(R.id.userpic);
            name=(EditText)findViewById(R.id.displayname);
            email=(EditText)findViewById(R.id.email);
            setprofile=(Button)findViewById(R.id.setProfile);
            selectpic=(Button)findViewById(R.id.selectpic);
            bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.user);

            Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
            imageView.setImageBitmap(bitmap);

            selectpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
                }
            });

            setprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uname=name.getText().toString();
                        String uemail=email.getText().toString();
                    if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(uemail) || TextUtils.isEmpty(uuserId)){
                        Toast.makeText(userProfile.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                        Toast.makeText(userProfile.this, uname+" "+uemail+" "+uuserId, Toast.LENGTH_SHORT).show();
                    }else {
                        registerUser(uuserId,uemail,uname);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath=data.getData();
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                userprofile.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(final String userId, String email, String displayName){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes=baos.toByteArray();
        userPhoto= Base64.encodeToString(imageBytes,Base64.DEFAULT);

        Call<userProfileResponse> call=apiInterface.setProfile(userId,email,displayName);
        call.enqueue(new Callback<userProfileResponse>() {
            @Override
            public void onResponse(Call<userProfileResponse> call, Response<userProfileResponse> response) {
                editor.putString("profileSet",userId);
                editor.commit();
                userProfileResponse response1=response.body();
                if(response.code()==200){
                    Intent intent=new Intent(userProfile.this,UserHome.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(userProfile.this, "Problem occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<userProfileResponse> call, Throwable t) {
                Toast.makeText(userProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Log.d("User data","Userid : "+userId+" Email : "+email+" displayName : "+displayName+" userPhoto : "+userPhoto);
    }
}
