package com.doctappo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import models.InsuranceProviderModel;
import models.InsuranceTypeModel;
import util.VJsonRequest;

public class RegisterActivity extends CommonActivity {

    private ArrayList<InsuranceTypeModel> insuranceTypeModelArrayList = new ArrayList<>();
    private ArrayList<InsuranceProviderModel> insuranceProviderModelArrayList = new ArrayList<>();

    private List<String> insuranceProvider = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapterProvider;

    private EditText editEmail, editPassword, editPhone, editFullname;
    private Spinner sp_insurnace_type, sp_insurnace_provider;
    private ProgressBar pb_type, pb_provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        allowBack();
        setHeaderTitle(getString(R.string.register_new));

        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        editFullname = (EditText) findViewById(R.id.txtFirstname);
        editPhone = (EditText) findViewById(R.id.txtPhone);
        sp_insurnace_type = (Spinner) findViewById(R.id.sp_register_insurance_type);
        sp_insurnace_provider = (Spinner) findViewById(R.id.sp_register_insurance_provider);

        pb_type = (ProgressBar) findViewById(R.id.pb_register_insurance_type);
        pb_provider = (ProgressBar) findViewById(R.id.pb_register_insurance_provider);

        sp_insurnace_type.setOnItemSelectedListener(new onItemSelected(sp_insurnace_type));
        sp_insurnace_provider.setOnItemSelectedListener(new onItemSelected(sp_insurnace_provider));

        arrayAdapterProvider = new ArrayAdapter<>(RegisterActivity.this, R.layout.row_spinner, R.id.tv_sp, insuranceProvider);
        sp_insurnace_provider.setAdapter(arrayAdapterProvider);

        makeGetInsuranceType();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    // attempt register user with required validation
    public void register() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            common.setToastMessage(getString(R.string.valid_required_email));
            focusView = editEmail;
            cancel = true;
        }
        if (!isValidEmail(email)) {
            common.setToastMessage(getString(R.string.valid_email));
            focusView = editEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            common.setToastMessage(getString(R.string.valid_required_password));
            focusView = editPassword;
            cancel = true;
        }
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
            params.put("user_email", email);
            params.put("user_password", password);
            if (sp_insurnace_type.getSelectedItemPosition() >= 0) {
                params.put("insurance_type_id", insuranceTypeModelArrayList.get(sp_insurnace_type.getSelectedItemPosition()).getInsurance_type_id());
            }
            if (sp_insurnace_provider.getSelectedItemPosition() >= 0) {
                params.put("insurance_provider_id", insuranceProviderModelArrayList.get(sp_insurnace_provider.getSelectedItemPosition()).getInsurance_provider_id());
            }

            // this class for handle request response thread and return response data
            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.REGISTER_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            JSONObject userdata = null;
                            try {
                                userdata = new JSONObject(responce);

                                if (userdata.has("is_email_varified")
                                        && userdata.getString("is_email_varified").equalsIgnoreCase("0")) {
                                    Intent verifyIntent = new Intent(RegisterActivity.this, SendVerificationActivity.class);
                                    verifyIntent.putExtra("user_id", userdata.getString("user_id"));
                                    verifyIntent.putExtra("user_email", userdata.getString("user_email"));
                                    startActivity(verifyIntent);
                                } else {
                                    common.setSession(ApiParams.COMMON_KEY, userdata.getString("user_id"));
                                    common.setSession(ApiParams.USER_FULLNAME, userdata.getString("user_fullname"));
                                    common.setSession(ApiParams.USER_EMAIL, userdata.getString("user_email"));
                                    common.setSession(ApiParams.USER_PHONE, userdata.getString("user_phone"));
                                    common.setSession(ApiParams.USER_INSURANCE_TYPE, userdata.getString("user_insurance_type_id"));
                                    common.setSession(ApiParams.USER_INSURANCE_PROVIDER, userdata.getString("user_insurance_provider_id"));

                                    finish();
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

                        List<String> insuranceType = new ArrayList<>();
                        for (InsuranceTypeModel insuranceTypeModel : insuranceTypeModelArrayList) {
                            insuranceType.add(insuranceTypeModel.getInsurance_type());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.row_spinner, R.id.tv_sp, insuranceType);
                        sp_insurnace_type.setAdapter(arrayAdapter);

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

                        for (InsuranceProviderModel insuranceProviderModel : insuranceProviderModelArrayList) {
                            insuranceProvider.add(insuranceProviderModel.getInsurance_provider());
                        }

                        arrayAdapterProvider.notifyDataSetChanged();

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
                    CommonActivity.DpToPx(RegisterActivity.this, 10F), 0,
                    CommonActivity.DpToPx(RegisterActivity.this, 10F));
            textView.setTextSize(16F);
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            View divider = (View) linearLayout.getChildAt(1);
            divider.setVisibility(View.GONE);

            if (spinner.getId() == R.id.sp_register_insurance_type) {
                makeGetInsuranceProvider(insuranceTypeModelArrayList.get(i).getInsurance_type_id());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
