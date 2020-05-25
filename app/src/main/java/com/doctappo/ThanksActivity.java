package com.doctappo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Config.ConstValue;
import adapters.BulletTextAdapter;
import models.ActiveModels;
import models.BulletTextModel;
import models.BusinessModel;

public class ThanksActivity extends CommonActivity implements AdapterView.OnItemSelectedListener{

    private TextView txtBusinessName, txtTotalTime, txtAmount, txtDate, txtTimeSlot;
    private ImageView imageView;
    private BusinessModel selected_business;

    String[] options ={"Choose Your Insurance Type", "Commecial Insurance", "Personal Injury(PIP)/ Auto Injury", "Workers Compensation /FCE/Work Hardening", "Cash Pay"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        setHeaderTitle(getString(R.string.thankyou));

        selected_business = ActiveModels.BUSINESS_MODEL;

        RecyclerView rv_point = (RecyclerView) findViewById(R.id.rv_thanks_point);
        txtAmount = (TextView) findViewById(R.id.totalAmount);
        txtTotalTime = (TextView) findViewById(R.id.totalTime);
        txtDate = (TextView) findViewById(R.id.textdate);
        txtTimeSlot = (TextView) findViewById(R.id.texttime);
        txtBusinessName = (TextView) findViewById(R.id.textBusinessName);
        imageView = (ImageView) findViewById(R.id.logoImage);

        txtAmount.setText(String.valueOf(common.get_service_total_amount() + Double.valueOf(selected_business.getBus_fee())));
        txtTotalTime.setText(common.get_service_total_times_string());
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/business/" + ActiveModels.BUSINESS_MODEL.getBus_logo()).into(imageView);
        txtBusinessName.setText(ActiveModels.BUSINESS_MODEL.getBus_title());

        txtDate.setText(getString(R.string.date) + " : " + getIntent().getExtras().getString("date"));
        txtTimeSlot.setText(getString(R.string.time) + " : " + getIntent().getExtras().getString("timeslot"));


        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,options);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


    }

    // home button click event
    public void orderFinish(View view) {
        ActiveModels.reset();
        Intent intent = new Intent(ThanksActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    // show my order click event
    public void myOrderActivity(View view) {
        ActiveModels.reset();
        Intent intent = new Intent(ThanksActivity.this, MyAppointmentsActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RecyclerView rv_point = (RecyclerView) findViewById(R.id.rv_thanks_point);
        TextView txt = findViewById(R.id.note);

        if (position == 0){
            Toast.makeText(ThanksActivity.this, "Select Your Insurance Type", Toast.LENGTH_LONG).show();
            txt.setMaxLines(Integer.MAX_VALUE);
        }

        else if (position == 1) {
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc), "https://startptnow.com/wp-content/uploads/2019/01/REGULAR-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.insurance_card_if_applicable), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.prescription), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
        }

        else if (position == 2){
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc_pip), "https://startptnow.com/wp-content/uploads/2019/01/REGULAR-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.attorney_info), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
        }

        else if (position == 3){
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc_wc_apply), "https://startptnow.com/wp-content/uploads/2019/01/REGULAR-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.adjuster_info), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
        }

        else if (position == 4){
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc), "https://startptnow.com/wp-content/uploads/2019/01/REGULAR-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
