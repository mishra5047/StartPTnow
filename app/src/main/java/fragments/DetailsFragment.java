package fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doctappo.MapActivity;
import com.doctappo.R;

import models.ActiveModels;
import models.BusinessModel;
import util.CommonClass;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class DetailsFragment extends Fragment {

    private CommonClass common;
    private Activity act;
    private Bundle args;
    private BusinessModel selected_business;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_details, container, false);
        act = getActivity();
        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        TextView txtPhone = (TextView) rootView.findViewById(R.id.textPhone);
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtPhone.getText())) ;
                startActivity(intent);
            }
        });

        TextView textDescription = (TextView) rootView.findViewById(R.id.textDescription);
        textDescription.setText(Html.fromHtml(selected_business.getBus_description()));

        TextView txtAddress = (TextView) rootView.findViewById(R.id.textAddress);

        txtPhone.setText(selected_business.getBus_contact());
        txtAddress.setText(selected_business.getBus_google_street());

        args = this.getArguments();

        Button locationBtn = (Button) rootView.findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

}
