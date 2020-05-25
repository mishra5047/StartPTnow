package fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctappo.DetailsSalonActivity;
import com.doctappo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import adapters.ServiceChargeAdapter;
import models.ActiveModels;
import models.BusinessModel;
import models.ServicesModel;
import util.CommonClass;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class PriceFragment extends Fragment {

    private CommonClass common;
    public static ArrayList<ServicesModel> mServiceArray;
    private ServiceChargeAdapter serviceChargeAdapter;
    private RecyclerView businessRecyclerView;

    private Activity act;
    private Bundle args;
    private BusinessModel selected_business;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_service, container, false);
        act = getActivity();

        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        args = this.getArguments();

        mServiceArray = new ArrayList<ServicesModel>();

        bundView(rootView);

        serviceChargeAdapter.setOnDataChangeListener(new ServiceChargeAdapter.OnDataChangeListener() {
            public void onDataChanged() {
                //do whatever here
                String time = "00:00:00";
                Double totalAmount = Double.parseDouble(selected_business.getBus_fee());

                for (int i = 0; i < mServiceArray.size(); i++) {
                    if (mServiceArray.get(i).isChecked()) {
                        time = common.totalTime(time, mServiceArray.get(i).getBusiness_approxtime());
                        totalAmount = totalAmount + Double.parseDouble(mServiceArray.get(i).getDiscountAmount());
                    }

                }
                String[] timesplit = time.split(":");
                ((DetailsSalonActivity) getActivity()).updatePrice(totalAmount, timesplit);
                //common.setSession(ApiParams.PRICE_CART, cart_items.toString());


            }
        });
        loadData();


        return rootView;
    }

    // get xml element and bind view
    public void bundView(View rootView) {
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        businessRecyclerView.setLayoutManager(layoutManager);

        serviceChargeAdapter = new ServiceChargeAdapter(getActivity(), mServiceArray);
        businessRecyclerView.setAdapter(serviceChargeAdapter);
    }

    // load price data from api
    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_SERVICES, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<ServicesModel>>() {
                        }.getType();
                        mServiceArray.clear();
                        mServiceArray.addAll((Collection<? extends ServicesModel>) gson.fromJson(responce, listType));
                        serviceChargeAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }


}
