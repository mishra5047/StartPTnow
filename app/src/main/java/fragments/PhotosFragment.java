package fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctappo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import adapters.PhotosAdapter;
import models.ActiveModels;
import models.BusinessModel;
import models.PhotosModel;
import util.CommonClass;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class PhotosFragment extends Fragment {

    private CommonClass common;
    private ArrayList<PhotosModel> mPhotoArray;
    private PhotosAdapter photoAdapter;
    private RecyclerView businessRecyclerView;

    private Activity act;
    private Bundle args;
    private BusinessModel selected_business;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        act = getActivity();

        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        args = this.getArguments();

        mPhotoArray = new ArrayList<>();
        bindView(rootView);
        loadData();

        return rootView;
    }

    // get view from xml
    public void bindView(View rootView) {
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final GridLayoutManager layoutManager
                = new GridLayoutManager(getActivity(), 2);
        businessRecyclerView.setLayoutManager(layoutManager);

        photoAdapter = new PhotosAdapter(getActivity(), mPhotoArray);
        businessRecyclerView.setAdapter(photoAdapter);
    }

    // load bussiness photo from api
    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_PHOTOS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PhotosModel>>() {
                        }.getType();
                        mPhotoArray.clear();
                        mPhotoArray.addAll((Collection<? extends PhotosModel>) gson.fromJson(responce, listType));
                        photoAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }


}
