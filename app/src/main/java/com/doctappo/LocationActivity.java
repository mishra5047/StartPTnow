package com.doctappo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Config.ApiParams;
import adapters.CategoryAdapter;
import adapters.LocalityAdapter;
import models.CategoryModel;
import models.LocalityModel;
import util.VJsonRequest;

public class LocationActivity extends CommonActivity {

    private ArrayList<LocalityModel> localityArray;
    private ArrayList<LocalityModel> searchArray;
    private RecyclerView recyclerView;
    private LocalityAdapter localityAdapter;

    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setHeaderTitle(getString(R.string.choose_locality));

        localityArray = new ArrayList<>();
        searchArray = new ArrayList<>();

        editSearch = (EditText) findViewById(R.id.editSearch);
        recyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        localityAdapter = new LocalityAdapter(this, searchArray);
        recyclerView.setAdapter(localityAdapter);
        loadData();
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                searchArray.clear();
                for (LocalityModel str : localityArray) {
                    Pattern p = Pattern.compile(editSearch.getText().toString().toLowerCase() + "(.*)");
                    Matcher m = p.matcher(str.getLocality().toLowerCase());
                    if (m.find()) {
                        searchArray.add(str);
                    }
                    //if(str.matches(txtSearch.getText()+".*")){
                    //	 searchArray.add(str);
                    //  }
                }

                localityAdapter.notifyDataSetChanged();
            }
        });
    }

    // load data from api
    public void loadData() {
        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(LocationActivity.this, ApiParams.GET_LOCALITY,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<LocalityModel>>() {
                        }.getType();
                        localityArray.clear();
                        searchArray.clear();
                        localityArray.addAll((Collection<? extends LocalityModel>) gson.fromJson(responce, listType));
                        searchArray.addAll((Collection<? extends LocalityModel>) gson.fromJson(responce, listType));
                        localityAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void VError(String responce) {

                    }
                });


    }
}
