package com.example.lcom151_two.veroapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.SearchResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.searchData;
import com.example.lcom151_two.veroapp.SearchDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    EditText searchValue;
    ImageView searchbtn;
    ApiInterface apiInterface;
    ArrayList<String> names,uIds;
    GridView searchGrid;

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,null);

        searchValue=(EditText)view.findViewById(R.id.searchvalue);
        searchbtn=(ImageView)view.findViewById(R.id.search);
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
        names=new ArrayList<String>();
        uIds=new ArrayList<String>();
        searchGrid=(GridView)view.findViewById(R.id.searchList);

        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search=searchValue.getText().toString();
                names.clear();
                uIds.clear();
                if(TextUtils.isEmpty(search)){
                    searchValue.requestFocus();
                }else {
                    Call<SearchResponseModel> call=apiInterface.search(search);
                    call.enqueue(new Callback<SearchResponseModel>() {
                        @Override
                        public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                            List<searchData> data=response.body().getMessage();

                            if(data.size()==0){
                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            names.clear();
//                            uIds.clear();
                            for(int i=0;i<data.size();i++){
                                Log.v("Data ","Name : "+data.get(i).getDisplayName()+"\nEmail : "+data.get(i).getEmail());
                                names.add(data.get(i).getDisplayName());
                                uIds.add(data.get(i).getUserId());
                            }
                            searchGrid.setAdapter(new SearchDisplayAdapter(getContext(),names,uIds));
                        }

                        @Override
                        public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                            Toast.makeText(getContext(), "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=searchValue.getText().toString();
                if(TextUtils.isEmpty(search)){
                    searchValue.requestFocus();
                }
//                else {
//                    Call<SearchResponseModel> call=apiInterface.search(search);
//                    call.enqueue(new Callback<SearchResponseModel>() {
//                        @Override
//                        public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
//                            List<searchData> data=response.body().getMessage();
//
//                            if(data.size()==0){
//                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            names.clear();
//                            uIds.clear();
//                            for(int i=0;i<data.size();i++){
//                                Log.v("Data ","Name : "+data.get(i).getDisplayName()+"\nEmail : "+data.get(i).getEmail());
//                                names.add(data.get(i).getDisplayName());
//                                uIds.add(data.get(i).getUserId());
//                            }
//                            searchGrid.setAdapter(new SearchDisplayAdapter(getContext(),names,uIds));
//                        }
//
//                        @Override
//                        public void onFailure(Call<SearchResponseModel> call, Throwable t) {
//                            Toast.makeText(getContext(), "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        });

        return view;
    }

}
