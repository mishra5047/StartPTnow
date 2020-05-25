package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.doctappo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import adapters.ReviewsAdapter;
import models.ActiveModels;
import models.BusinessModel;
import models.ReviewsModel;
import util.CommonClass;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class ReviewsFragment extends Fragment {

    private CommonClass common;
    private ArrayList<ReviewsModel> mReviewsArray;
    private ReviewsAdapter serviceChargeAdapter;
    private RecyclerView businessRecyclerView;

    private Activity act;
    private Bundle args;
    private BusinessModel selected_business;

    private RatingBar ratingbar;
    private EditText txtComment;
    private Button btnAddReview;
    private RelativeLayout topLayout;
    protected AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        act = getActivity();

        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        args = this.getArguments();

        mReviewsArray = new ArrayList<ReviewsModel>();
        bundView(rootView);

        loadData();

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviewDialog();
            }
        });

        return rootView;
    }

    // load view from xml
    public void bundView(View rootView) {
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        businessRecyclerView.setLayoutManager(layoutManager);

        serviceChargeAdapter = new ReviewsAdapter(getActivity(), mReviewsArray);
        businessRecyclerView.setAdapter(serviceChargeAdapter);

        topLayout = (RelativeLayout) rootView.findViewById(R.id.topLayout);
        btnAddReview = (Button) rootView.findViewById(R.id.addreview);
    }

    // get review data from api and bind recyclerview
    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_REVIEWS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<ReviewsModel>>() {
                        }.getType();
                        mReviewsArray.clear();
                        mReviewsArray.addAll((Collection<? extends ReviewsModel>) gson.fromJson(responce, listType));
                        serviceChargeAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }

    // show review dialog
    public void openReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        ratingbar = (RatingBar) view.findViewById(R.id.ratingBar1);
        txtComment = (EditText) view.findViewById(R.id.editText1);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(getString(R.string.add_review), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (!TextUtils.isEmpty(txtComment.getText()))
                            sendData();
                        else
                            txtComment.setError(getString(R.string.required));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    // send review data to server
    public void sendData() {
        HashMap<String, String> params = new HashMap<>();

        params.put("bus_id", selected_business.getBus_id());
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("reviews", txtComment.getText().toString());
        params.put("rating", String.valueOf(ratingbar.getRating()));

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.ADD_BUSINESS_REVIEWS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ReviewsModel>() {
                        }.getType();
                        mReviewsArray.add((ReviewsModel) gson.fromJson(responce, listType));
                        serviceChargeAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                            alertDialog = null;
                        }
                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
