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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.GlobalClass;
import com.example.lcom151_two.veroapp.ModalClasses.SearchModalClass;
import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.SearchDataModel;
import com.example.lcom151_two.veroapp.apiClasses.SearchResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.SearchUserResponseModel;
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
    ArrayList<SearchModalClass> searchModalClass,smodal1;
    ListView searchGrid;
    TextView noUser;
    SearchDisplayAdapter adapter;

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,null);

        initViews(view);

        getUsers();

        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String query=searchValue.getText().toString();
                if(query.length()>0){
                    ArrayList<SearchModalClass> smodal=new ArrayList<SearchModalClass>();

                    for(SearchModalClass sm : searchModalClass){
                        if(sm.getDisplayName().toLowerCase().contains(query) || sm.getUserId().contains(query) || sm.getEmail().toLowerCase().contains(query))
                            smodal.add(sm);
                    }
                    if(smodal.size()>0){
                        noUser.setVisibility(View.GONE);
                        searchGrid.setVisibility(View.VISIBLE);
                        adapter.filteredData(smodal);
                    }else {
                        noUser.setVisibility(View.VISIBLE);
                        noUser.setText("No user found");
                        searchGrid.setVisibility(View.GONE);
                    }
                }else {
                    noUser.setVisibility(View.GONE);
                    //searchGrid.setAdapter(new SearchDisplayAdapter(getContext(),smodal1));
                    adapter.filteredData(smodal1);
                }

                Log.i("Data",searchModalClass.size()+"");
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=searchValue.getText().toString();
                if(TextUtils.isEmpty(search)){
                    searchValue.requestFocus();
                }
            }
        });

        return view;
    }

    public void initViews(View view){
        searchValue=(EditText)view.findViewById(R.id.searchvalue);
        searchbtn=(ImageView)view.findViewById(R.id.search);
        searchModalClass=new ArrayList<SearchModalClass>();
        smodal1=new ArrayList<SearchModalClass>();
        searchGrid=(ListView) view.findViewById(R.id.searchList);
        noUser=(TextView)view.findViewById(R.id.noUserFound);
    }

//    public void search(){
//        String query=searchValue.getText().toString();
//        searchModalClass.clear();
//        if(TextUtils.isEmpty(query)){
//            searchValue.requestFocus();
//        }else {
//            try {
//                Call<SearchUserResponseModel> call = GlobalClass.apiInterface.searchUser(query);
//                call.enqueue(new Callback<SearchUserResponseModel>() {
//                    @Override
//                    public void onResponse(Call<SearchUserResponseModel> call, Response<SearchUserResponseModel> response) {
//                        if (response.code() == 200) {
//                            List<SearchDataModel> data=response.body().getMessage();
//                            if(data.size()==0){
//                                noUser.setVisibility(View.VISIBLE);
//                                noUser.setText("No data found");
//                                return;
//                            }else {
//                                noUser.setVisibility(View.GONE);
//                            }
//                            for(int i=0;i<data.size();i++){
//                                searchModalClass.add(new SearchModalClass(data.get(i).getUserId(),data.get(i).getUserProfilePhoto(),data.get(i).getDisplayName()));
//                            }
//                            searchGrid.setAdapter(new SearchDisplayAdapter(getContext(),searchModalClass));
//                        } else {
//                            Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SearchUserResponseModel> call, Throwable t) {
//                        Toast.makeText(getContext(), "Failure occurred " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }catch (Exception e){
//                Toast.makeText(getContext(), "Exception occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("Exception",e.getMessage());
//            }
//        }
//    }

    public void getUsers(){
        searchModalClass.clear();
            try {
                Call<SearchUserResponseModel> call = GlobalClass.apiInterface.searchUser("9");
                call.enqueue(new Callback<SearchUserResponseModel>() {
                    @Override
                    public void onResponse(Call<SearchUserResponseModel> call, Response<SearchUserResponseModel> response) {
                        if (response.code() == 200) {
                            List<SearchDataModel> data=response.body().getMessage();
                            if(data.size()==0){
                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for(int i=0;i<data.size();i++){
                                searchModalClass.add(new SearchModalClass(data.get(i).getUserId(),data.get(i).getUserProfilePhoto(),data.get(i).getDisplayName(),data.get(i).getEmail()));
                            }
                            adapter=new SearchDisplayAdapter(getContext(),smodal1);
                            searchGrid.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchUserResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure occurred " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                Toast.makeText(getContext(), "Exception occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Exception",e.getMessage());
            }
        }

}
