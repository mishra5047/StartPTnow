package fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doctappo.DetailsSalonActivity;
import com.doctappo.MainActivity;
import com.doctappo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import adapters.BusinessListAdapter;
import adapters.CategoryAdapter;
import models.ActiveModels;
import models.BusinessModel;
import models.CategoryModel;
import util.CommonClass;
import util.GPSTracker;
import util.NameValuePair;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class ListFragment extends Fragment {

    private ArrayList<BusinessModel> postItems;

    private CommonClass common;

    public int current_page;
    public boolean loadingMore;
    public boolean stopLoadingData;
    public boolean is_first_time;
    public int number_of_item;

    private RecyclerView businessRecyclerView;
    private BusinessListAdapter adapter;
    private ProgressBar progressBar;

    private Activity act;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_list_business, container, false);
        act = getActivity();
        common = new CommonClass(act);
        postItems = new ArrayList<BusinessModel>();


        number_of_item = 10;
        current_page = 0;
        loadingMore = true;
        stopLoadingData = true;
        is_first_time = true;

        bindView(rootView);

        loadGetResult();

        return rootView;
    }

    // load xml element in view
    public void bindView(View rootView) {
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        businessRecyclerView.setLayoutManager(layoutManager);

        adapter = new BusinessListAdapter(getActivity(), postItems);
        businessRecyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        businessRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    int pastVisiblesItems, visibleItemCount, totalItemCount;
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!(loadingMore)) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (stopLoadingData == false) {
                                // FETCH THE NEXT BATCH OF FEEDS
                                is_first_time = false;
                                loadGetResult();
                            }
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    // load business data from business api
    public void loadGetResult() {
        current_page = 0;
        loadingMore = true;
        stopLoadingData = true;
        is_first_time = true;

        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        if (getArguments() != null && getArguments().containsKey("search"))
            params.put("search", getArguments().getString("search"));

        //if (type !=null && !type.equalsIgnoreCase("0"))
        //    params.put("type","page_type_id");

        if (getArguments().containsKey("cat_id"))
            params.put("cat_id", getArguments().getString("cat_id"));
        if (getArguments().containsKey("locality_id"))
            params.put("locality_id", getArguments().getString("locality_id"));
        int radius = 60;
        if (common.containKeyInSession("radius")) {
            radius = common.getSessionInt("radius");
            if (radius <= 0) {
                radius = 60;
            }
        }

        if (getArguments().containsKey("lat"))
            params.put("lat", getArguments().getString("lat"));
        if (getArguments().containsKey("lon"))
            params.put("lon", getArguments().getString("lon"));

        params.put("rad", String.valueOf(radius));

        params.put("offcet", String.valueOf(current_page));
        params.put("number_row", String.valueOf(number_of_item));

        Log.i("List", "loadGetResult: " + params);

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_LIST, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<BusinessModel>>() {
                        }.getType();
                        ArrayList<BusinessModel> arraylist = (ArrayList<BusinessModel>) gson.fromJson(responce, listType);


                        if (is_first_time) {
                            postItems.clear();
                        }
                        if (arraylist.size() < number_of_item) {
                            stopLoadingData = true;
                            loadingMore = true;
                        } else {
                            stopLoadingData = false;
                            loadingMore = false;
                        }
                        postItems.addAll(arraylist);
                        ActiveModels.LIST_BUSINESS_MODEL = postItems;
                        progressBar.setVisibility(View.GONE);
                        if (postItems != null) {
                            current_page = current_page + number_of_item;
                            adapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void VError(String responce) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updatelist();
            adapter.notifyDataSetChanged();
        }
    }

}
