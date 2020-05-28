package com.doctappo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Config.ApiParams;
import Config.ConstValue;
import models.ActiveModels;
import util.NameValuePair;
import util.NotifyService;
import util.VJsonRequest;

public class PersonInfoActivity extends CommonActivity {

    private EditText editEmail, editPhone, editFullname, editNote;

    private String choose_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        allowBack();
        setHeaderTitle(getString(R.string.contact_info));

        choose_date = getIntent().getExtras().getString("date");
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editFullname = (EditText) findViewById(R.id.txtFirstname);
        editPhone = (EditText) findViewById(R.id.txtPhone);
        editNote = (EditText) findViewById(R.id.txtNote);

        editEmail.setText(common.getSession(ApiParams.USER_EMAIL));
        editFullname.setText(common.getSession(ApiParams.USER_FULLNAME));
        editPhone.setText(common.getSession(ApiParams.USER_PHONE));

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    // attempt book appointment with required validation
    public void register() {

        String email = editEmail.getText().toString();
        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            common.setToastMessage(getString(R.string.valid_required_email));
            focusView = editEmail;
            cancel = true;
        } else if (!isValidEmail(email)) {
            common.setToastMessage(getString(R.string.valid_email));
            focusView = editEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullname)) {
            common.setToastMessage(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            common.setToastMessage(getString(R.string.valid_required_phone));
            focusView = editPhone;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("doct_id", ActiveModels.DOCTOR_MODEL.getDoct_id());
            map.put("bus_id", ActiveModels.BUSINESS_MODEL.getBus_id());
            map.put("user_fullname", fullname);
            map.put("user_phone", phone);
            map.put("user_email", email);
            map.put("start_time", ActiveModels.SELECTED_SLOT.getSlot());
            map.put("time_token", String.valueOf(ActiveModels.SELECTED_SLOT.getTime_token()));
            map.put("appointment_date", choose_date);
            map.put("user_id", common.get_user_id());
            map.put("note", editNote.getText().toString());

            String str_services = "";
            for (int i = 0; i < ActiveModels.LIST_SERVICES_MODEL.size(); i++) {
                if (ActiveModels.LIST_SERVICES_MODEL.get(i).isChecked()) {
                    if (str_services.equalsIgnoreCase("")) {
                        str_services = ActiveModels.LIST_SERVICES_MODEL.get(i).getId();
                    } else {
                        str_services = str_services + "," + ActiveModels.LIST_SERVICES_MODEL.get(i).getId();
                    }
                }
            }
            map.put("services", str_services);
            //CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.BOOKAPPOINTMENT_URL, handler, true, this);
            //commonTask.execute();
            if (ConstValue.enable_paypal) {
                // this class for handle request response thread and return response data
                VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.BOOKAPPOINTMENT_TEMP_URL, map,
                        new VJsonRequest.VJsonResponce() {
                            @Override
                            public void VResponce(String responce) {
                                Intent intent = new Intent(PersonInfoActivity.this, PaymentActivity.class);
                                intent.putExtra("order_details", responce);
                                startActivity(intent);
                            }

                            @Override
                            public void VError(String responce) {
                                common.setToastMessage(responce);
                            }
                        });
            } else {
                // this class for handle request response thread and return response data
                VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.BOOKAPPOINTMENT_URL, map,
                        new VJsonRequest.VJsonResponce() {
                            @Override
                            public void VResponce(String responce) {

                                JSONObject appointmentData = null;
                                try {
                                    appointmentData = new JSONObject(responce);


                                    String messageType = getString(R.string.appoitnment_confirm_message_part1) + appointmentData.getString("id") + " " + getString(R.string.appoitnment_confirm_message_part1);
                                    common.setToastMessage(getString(R.string.appoitnment_confirm_message_part1) + appointmentData.getString("id") + " " + getString(R.string.appoitnment_confirm_message_part1));
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                                    SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.UK);
                                    try {
                                        Date testDate = formatter.parse(choose_date);
                                        Date testTime = formatter2.parse(ActiveModels.SELECTED_SLOT.getSlot());
                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


                                        Calendar myAlarmDate = Calendar.getInstance();
                                        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                                        myAlarmDate.set(testDate.getYear(), testDate.getMonth(), testDate.getDate(), testTime.getHours(), testTime.getMinutes(), 0);

                                        Intent _myIntent = new Intent(PersonInfoActivity.this, NotifyService.class);
                                        _myIntent.putExtra("MyMessage", messageType);
                                        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(PersonInfoActivity.this, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), _myPendingIntent);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(PersonInfoActivity.this, ThanksActivity.class);
                                    intent.putExtra("date", choose_date);
                                    intent.putExtra("timeslot", ActiveModels.SELECTED_SLOT.getSlot());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void VError(String responce) {
                                common.setToastMessage(responce);
                            }
                        });
            }
        }

    }

    // return true if email is valid otherwise return false
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
