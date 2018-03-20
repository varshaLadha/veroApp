package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.apiClasses.*;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.FollowingUserResponseModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDisplayAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> username,fuserId,uids,profile;
    SharedPreferences sp;
    String userId,url;
    ApiInterface apiInterface;

    public SearchDisplayAdapter(Context context, ArrayList<String> username,ArrayList<String> uids,ArrayList<String> profile){
        this.context=context;
        this.username=username;
        this.uids=uids;
        this.profile=profile;

        sp=context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId=sp.getString("userId","");
        fuserId=new ArrayList<String>();
        url="http://192.168.200.147:3005/profile/";
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView userDisplay;
        final Button follow;
        final ImageView userPic;
        try{
            Gson gson=new Gson();
            String object=sp.getString("userDetail","");
            UserDataModelClass udm=gson.fromJson(object,UserDataModelClass.class);
            final String name=udm.displayName;

            convertView= LayoutInflater.from(context).inflate(R.layout.searchdata,null);
            userDisplay=convertView.findViewById(R.id.username);
            follow=convertView.findViewById(R.id.follow);
            userPic=convertView.findViewById(R.id.userpic);

            retrofit2.Call<FollowingUserResponseModel> call=apiInterface.following(userId);
            call.enqueue(new Callback<FollowingUserResponseModel>() {
                @Override
                public void onResponse(retrofit2.Call<FollowingUserResponseModel> call, Response<FollowingUserResponseModel> response) {
                    if(response.body().getStatus()==1) {
                        List<FollowingUsersid> data = response.body().getMessage();
                        for (int i = 0; i < data.size(); i++) {
                            fuserId.add(data.get(i).getFuserId());
                        }
                        try {
                            if(!TextUtils.isEmpty(profile.get(position))) {
                                Picasso.get()
                                        .load(url + profile.get(position))
                                        .into(userPic);
                            }else {
                                userPic.setImageResource(R.drawable.user);
                            }

                            for (int i = 0; i < fuserId.size(); i++) {
                                if (fuserId.get(i).equals(uids.get(position))) {
                                    userDisplay.setText(username.get(position));
                                    follow.setText("Following");
                                    return;
                                } else if (username.get(position).equals(name)) {
                                    userDisplay.setText(username.get(position));
                                    follow.setVisibility(View.GONE);
                                } else {
                                    userDisplay.setText(username.get(position));
                                    follow.setText("Follow");
                                }
                            }
                        }catch (Exception e){
                            //Toast.makeText(context, "Exception occurred : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<FollowingUserResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Failure occured "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(follow.getText().equals("Following")){
                        //Toast.makeText(context, "Already following user", Toast.LENGTH_SHORT).show();
                    }else {
                        Call<responseModel> call1=apiInterface.followUser(userId,uids.get(position));
                        call1.enqueue(new Callback<responseModel>() {
                            @Override
                            public void onResponse(Call<responseModel> call, Response<responseModel> response) {
                                    if(response.body().getStatus()==1){
                                        follow.setText("Following");
                                    }else {
                                        Toast.makeText(context, "Problem", Toast.LENGTH_SHORT).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<responseModel> call, Throwable t) {
                                Toast.makeText(context, "Failure occured : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }catch (Exception e){
            Toast.makeText(context, "Problem occurred : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error : ",e.getStackTrace().toString());
        }


        return convertView;
    }
}
