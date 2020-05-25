package fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import adapters.DoctorAdapter;
import dialogues.DoctorInfoDialog;
import models.ActiveModels;
import models.BusinessModel;
import models.DoctorModel;
import util.CommonClass;
import util.RecyclerItemClickListener;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class DoctorsFragment extends Fragment {

    private CommonClass common;
    private Activity act;
    private Bundle args;
    private BusinessModel selected_business;

    public static ArrayList<DoctorModel> mDoctorArray;
    private DoctorAdapter doctorAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);
        act = getActivity();
        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;
        mDoctorArray = new ArrayList<DoctorModel>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        doctorAdapter = new DoctorAdapter(getActivity(), mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DoctorInfoDialog doctorInfoDialog = new DoctorInfoDialog(getActivity(), mDoctorArray.get(position));
                doctorInfoDialog.show();
            }
        }));

        loadData();

        args = this.getArguments();

        return rootView;
    }

    // get doctor list
    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());
        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.GET_DOCTORS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<DoctorModel>>() {
                        }.getType();

                        mDoctorArray.clear();
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(responce, listType));
                        doctorAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }

}
