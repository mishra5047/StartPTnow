package com.doctappo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Config.ApiParams;
import models.InsuranceProviderModel;
import models.InsuranceTypeModel;
import util.VJsonRequest;

public class UpdateProfileActivity extends CommonActivity {

    private ArrayList<InsuranceTypeModel> insuranceTypeModelArrayList = new ArrayList<>();
    private ArrayList<InsuranceProviderModel> insuranceProviderModelArrayList = new ArrayList<>();

    private List<String> insuranceProvider = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapterProvider;

    private EditText editEmail, editPhone, editFullname, editDOB;
    private Spinner sp_insurnace_type, sp_insurnace_provider;
    private ProgressBar pb_type, pb_provider;

    private String insurance_type_id = "", insurance_type_provider = "";
    private String getdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        allowBack();
        setHeaderTitle(getString(R.string.update_profile));

        editEmail = (EditText) findViewById(R.id.txtEmail);
        editFullname = (EditText) findViewById(R.id.txtFirstname);
        editPhone = (EditText) findViewById(R.id.txtPhone);
        editDOB = (EditText) findViewById(R.id.txtDOB);
        sp_insurnace_type = (Spinner) findViewById(R.id.sp_profile_insurance_type);
        sp_insurnace_provider = (Spinner) findViewById(R.id.sp_profile_insurance_provider);

        pb_type = (ProgressBar) findViewById(R.id.pb_profile_insurance_type);
        pb_provider = (ProgressBar) findViewById(R.id.pb_profile_insurance_provider);

        sp_insurnace_type.setOnItemSelectedListener(new onItemSelected(sp_insurnace_type));
        sp_insurnace_provider.setOnItemSelectedListener(new onItemSelected(sp_insurnace_provider));

        arrayAdapterProvider = new ArrayAdapter<>(this, R.layout.row_spinner, R.id.tv_sp, insuranceProvider);
        sp_insurnace_provider.setAdapter(arrayAdapterProvider);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.USERDATA_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);
                            editEmail.setText(userdata.getString("user_email"));
                            editFullname.setText(userdata.getString("user_fullname"));
                            editPhone.setText(userdata.getString("user_phone"));
                            insurance_type_id = userdata.getString("user_insurance_type_id");
                            insurance_type_provider = userdata.getString("user_insurance_provider_id");
                            getdate = userdata.getString("user_bdate");

                            editEmail.setEnabled(false);

                            makeGetInsuranceType();

                            SimpleDateFormat simpleDateFormatIN = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            SimpleDateFormat simpleDateFormatOUT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                            try {
                                Date date = simpleDateFormatIN.parse(getdate);
                                editDOB.setText(simpleDateFormatOUT.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

    }

    private void chooseDate() {

        String oldDate = editDOB.getText().toString();

        final Calendar c = Calendar.getInstance();
        if (!oldDate.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                Date date = simpleDateFormat.parse(getdate);
                c.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 10);
            }
        } else {
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 10);
        }
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        SimpleDateFormat simpleDateFormatIN = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        SimpleDateFormat simpleDateFormatOUT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                        try {
                            Date date = simpleDateFormatIN.parse(getdate);
                            editDOB.setText(simpleDateFormatOUT.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    // attempt update profile with validations
    public void register() {

        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        String dob = editDOB.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(fullname)) {
            common.setToastMessage(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            common.setToastMessage(getString(R.string.valid_required_password));
            focusView = editPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(dob)) {
            common.setToastMessage(getString(R.string.valid_required_dob));
            focusView = editDOB;
            cancel = true;
        }
        if (sp_insurnace_type.getSelectedItemPosition() < 0) {
            common.setToastMessage(getString(R.string.valid_required_insurance_type));
            focusView = sp_insurnace_type;
            cancel = true;
        }

        if (sp_insurnace_provider.getSelectedItemPosition() < 0) {
            common.setToastMessage(getString(R.string.valid_required_insurance_provider));
            focusView = sp_insurnace_provider;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_fullname", fullname);
            params.put("user_phone", phone);
            params.put("user_bdate", getdate);
            params.put("user_id", common.get_user_id());
            if (sp_insurnace_type.getSelectedItemPosition() >= 0) {
                params.put("insurance_type_id", insuranceTypeModelArrayList.get(sp_insurnace_type.getSelectedItemPosition()).getInsurance_type_id());
            }
            if (sp_insurnace_provider.getSelectedItemPosition() >= 0) {
                params.put("insurance_provider_id", insuranceProviderModelArrayList.get(sp_insurnace_provider.getSelectedItemPosition()).getInsurance_provider_id());
            }

            // this class for handle request response thread and return response data
            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.UPDATEPROFILE_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            common.setSession(ApiParams.USER_FULLNAME, editFullname.getText().toString());
                            common.setSession(ApiParams.USER_PHONE, editPhone.getText().toString());

                            common.setToastMessage(getResources().getString(R.string.profile_update_success));

                        }

                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });
        }

    }

    // check and return true if email is valid otherwise return false
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void makeGetInsuranceType() {
        HashMap<String, String> params = new HashMap<>();

        sp_insurnace_type.setVisibility(View.GONE);
        pb_type.setVisibility(View.VISIBLE);

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_INSURANCE_TYPE_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<InsuranceTypeModel>>() {
                        }.getType();

                        insuranceTypeModelArrayList.addAll(gson.fromJson(responce, type));

                        int selectedPosition = 0, position = 0;
                        List<String> insuranceType = new ArrayList<>();
                        for (InsuranceTypeModel insuranceTypeModel : insuranceTypeModelArrayList) {
                            insuranceType.add(insuranceTypeModel.getInsurance_type());
                            if (insuranceTypeModel.getInsurance_type_id().equalsIgnoreCase(insurance_type_id)) {
                                selectedPosition = position;
                            }
                            position++;
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(UpdateProfileActivity.this, R.layout.row_spinner, R.id.tv_sp, insuranceType);
                        sp_insurnace_type.setAdapter(arrayAdapter);
                        sp_insurnace_type.setSelection(selectedPosition);

                        sp_insurnace_type.setVisibility(View.VISIBLE);
                        pb_type.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                        sp_insurnace_type.setVisibility(View.VISIBLE);
                        pb_type.setVisibility(View.GONE);
                        sp_insurnace_provider.setVisibility(View.VISIBLE);
                        pb_provider.setVisibility(View.GONE);
                    }
                });
    }

    private void makeGetInsuranceProvider(String type_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type_id", type_id);

        insuranceProviderModelArrayList.clear();
        insuranceProvider.clear();

        sp_insurnace_provider.setVisibility(View.GONE);
        pb_provider.setVisibility(View.VISIBLE);

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_INSURANCE_PROVIDERS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<InsuranceProviderModel>>() {
                        }.getType();

                        insuranceProviderModelArrayList.addAll(gson.fromJson(responce, type));

                        int selectedPosition = 0, position = 0;
                        for (InsuranceProviderModel insuranceProviderModel : insuranceProviderModelArrayList) {
                            insuranceProvider.add(insuranceProviderModel.getInsurance_provider());
                            if (insuranceProviderModel.getInsurance_provider_id().equalsIgnoreCase(insurance_type_provider)) {
                                selectedPosition = position;
                            }
                            position++;
                        }
                        arrayAdapterProvider.notifyDataSetChanged();
                        sp_insurnace_provider.setSelection(selectedPosition);

                        sp_insurnace_provider.setVisibility(View.VISIBLE);
                        pb_provider.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                        sp_insurnace_provider.setVisibility(View.VISIBLE);
                        pb_provider.setVisibility(View.GONE);
                    }
                });
    }

    private class onItemSelected implements AdapterView.OnItemSelectedListener {

        private Spinner spinner;

        onItemSelected(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            LinearLayout linearLayout = (LinearLayout) adapterView.getChildAt(0);
            TextView textView = (TextView) linearLayout.getChildAt(0);
            textView.setPadding(0,
                    CommonActivity.DpToPx(UpdateProfileActivity.this, 10F), 0,
                    CommonActivity.DpToPx(UpdateProfileActivity.this, 10F));
            textView.setTextSize(16F);
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            View divider = (View) linearLayout.getChildAt(1);
            divider.setVisibility(View.GONE);

            if (spinner.getId() == R.id.sp_profile_insurance_type) {
                makeGetInsuranceProvider(insuranceTypeModelArrayList.get(i).getInsurance_type_id());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
