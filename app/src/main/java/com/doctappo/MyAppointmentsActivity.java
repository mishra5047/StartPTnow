package com.doctappo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Config.ApiParams;
import adapters.MyAppointmentAdapter;
import models.AppointmentModel;
import models.ServicesModel;
import util.CommonClass;
import util.SwipeDetector;
import util.VJsonRequest;

public class MyAppointmentsActivity extends CommonActivity {

    private ArrayList<AppointmentModel> appointmentArray;
    private RecyclerView.Adapter adapter;
    private SwipeDetector swipeDetector;
    private AlertDialog alertDialog;
    private int selected_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        appointmentArray = new ArrayList<>();

        allowBack();
        setHeaderTitle(getString(R.string.my_appointments));
        swipeDetector = new SwipeDetector();

        //ListView listView = (ListView) findViewById(R.id.listview);

        //listView.setOnTouchListener(swipeDetector);
        RecyclerView rv_appointment = (RecyclerView) findViewById(R.id.listview);
        rv_appointment.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAppointmentAdapter(this, appointmentArray);
        ((MyAppointmentAdapter) adapter).setMode(Attributes.Mode.Single);
        rv_appointment.setAdapter(adapter);
        /*listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        deleteConfirm(position);
                    } else {

                    }
                }

            }
        });*/


        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.MYAPPOINTMENTS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AppointmentModel>>() {
                        }.getType();
                        appointmentArray.clear();
                        appointmentArray.addAll((Collection<? extends AppointmentModel>) gson.fromJson(responce, listType));
                        adapter.notifyDataSetChanged();

                        //appointmentArray =  (ArrayList<HashMap<String,String>>) message.getData().getSerializable(ApiParams.PARM_DATA);
                        //common.parseObject("my_appointments",appointmentArray);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }

    // show appointment delete dialog
    public void deleteConfirm(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cancel_confirmation)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        selected_index = position;
                        deleteRow();


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    // make delete appointment from server
    public void deleteRow() {
        AppointmentModel map = appointmentArray.get(selected_index);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("app_id", map.getId());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.CANCELAPPOINTMENTS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        appointmentArray.remove(selected_index);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }

    class AppointmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (appointmentArray == null)
                return 0;
            else
                return appointmentArray.size();
        }

        @Override
        public AppointmentModel getItem(int i) {
            return appointmentArray.get(i);

        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_appointment, null);
            }
            AppointmentModel appointment = appointmentArray.get(i);

            TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
            TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            TextView txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            TextView payment = (TextView) view.findViewById(R.id.payment);
            TextView txtClinic = (TextView) view.findViewById(R.id.txtClinic);
            TextView txtClinicAddress = (TextView) view.findViewById(R.id.txtClincAddress);
            TextView txtClinicPhone = (TextView) view.findViewById(R.id.txtClincPhone);

            txtDate.setText(parseDateToddMM(appointment.getAppointment_date()));
            txtTime.setText(CommonClass.parseTime(appointment.getStart_time()));
            txtName.setText(appointment.getApp_name());
            txtPhone.setText(appointment.getApp_phone());

            txtClinic.setText(appointment.getBus_title());
            txtClinicAddress.setText(appointment.getDoct_name());
            txtClinicPhone.setText(appointment.getDoct_degree());
            if (appointment.getPayment_ref() != null && !appointment.getPayment_ref().equalsIgnoreCase("")) {
                payment.setText(R.string.paid);
            } else {
                payment.setText(R.string.unpaid);
            }
            return view;
        }
    }

    public String parseDateToddMM(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
