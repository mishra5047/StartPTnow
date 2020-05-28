package com.doctappo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        setHeaderTitle(getString(R.string.thankyou));

        String first, second, third, fourth, fifth;
        first = getResources().getString(R.string.spinner_0);
        second = getResources().getString(R.string.spinner_1);
        third = getResources().getString(R.string.spinner_2);
        fourth = getResources().getString(R.string.spinner_3);
        fifth = getResources().getString(R.string.spinner_4);

        String[] options ={ first, second, third, fourth, fifth};

        RecyclerView rv_point = (RecyclerView) findViewById(R.id.rv_thanks_point);
        txtAmount = (TextView) findViewById(R.id.totalAmount);
        txtTotalTime = (TextView) findViewById(R.id.totalTime);
        txtDate = (TextView) findViewById(R.id.textdate);
        txtTimeSlot = (TextView) findViewById(R.id.texttime);
        txtBusinessName = (TextView) findViewById(R.id.textBusinessName);
        imageView = (ImageView) findViewById(R.id.logoImage);

//        txtAmount.setText(String.valueOf(common.get_service_total_amount() + Double.parseDouble(selected_business.getBus_fee())));
//        txtTotalTime.setText(common.get_service_total_times_string());
        selected_business = ActiveModels.BUSINESS_MODEL;
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/business/" + ActiveModels.BUSINESS_MODEL.getBus_logo()).into(imageView);
         txtBusinessName.setText(ActiveModels.BUSINESS_MODEL.getBus_title());

       txtDate.setText(getString(R.string.date) + " : " + getIntent().getExtras().getString("date"));
        txtTimeSlot.setText(getString(R.string.time) + " : " + getIntent().getExtras().getString("timeslot"));


        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,R.layout.my_spinner,options);
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
        TextView thing = findViewById(R.id.things);

        if (position == 0){
            Toast.makeText(ThanksActivity.this, "Select Your Insurance Type", Toast.LENGTH_LONG).show();
            txt.setMaxLines(Integer.MAX_VALUE);
            txt.setText(R.string.note_insurance);
            thing.setVisibility(View.INVISIBLE);
            rv_point.setVisibility(View.INVISIBLE);
        }

        else if (position == 1) {
            thing.setVisibility(View.VISIBLE);
            thing.setMaxLines(Integer.MAX_VALUE);
            rv_point.setVisibility(View.VISIBLE);
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(("REGULAR PATIENT INFORMATION"), "https://startptnow.com/wp-content/uploads/2019/01/REGULAR-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.insurance_card_if_applicable), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.prescription), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
            txt.setMaxLines(0);
        }

        else if (position == 2){
            thing.setVisibility(View.VISIBLE);
            thing.setMaxLines(Integer.MAX_VALUE);
            rv_point.setVisibility(View.VISIBLE);
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc_pip), "https://startptnow.com/wp-content/uploads/2019/01/PIP-LITIGATION-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.attorney_info), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
            txt.setMaxLines(0);
        }

        else if (position == 3){
            thing.setVisibility(View.VISIBLE);
            thing.setMaxLines(Integer.MAX_VALUE);
            rv_point.setVisibility(View.VISIBLE);
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.ptsmc_wc_apply), "https://startptnow.com/wp-content/uploads/2019/01/WORKERS-COMPENSATION-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.adjuster_info), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
            txt.setMaxLines(0);
        }

        else if (position == 4){
            thing.setVisibility(View.VISIBLE);
            thing.setMaxLines(Integer.MAX_VALUE);
            rv_point.setVisibility(View.VISIBLE);
            ArrayList<BulletTextModel> bulletTextModelArrayList = new ArrayList<>();
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.cash_pay), "https://startptnow.com/wp-content/uploads/2019/01/SELF-PAY-PATIENT-INFORMATION-medical-updated.pdf"));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.photo_id), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.current_medications), ""));
            bulletTextModelArrayList.add(new BulletTextModel(getResources().getString(R.string.physican_referral), ""));
            rv_point.setLayoutManager(new LinearLayoutManager(this));
            BulletTextAdapter bulletTextAdapter = new BulletTextAdapter(this, bulletTextModelArrayList);
            rv_point.setAdapter(bulletTextAdapter);
            txt.setMaxLines(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(ThanksActivity.this, "Select Your Insurance Type", Toast.LENGTH_LONG).show();
    }
}
