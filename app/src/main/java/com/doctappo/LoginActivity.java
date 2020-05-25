package com.doctappo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Config.ApiParams;
import util.VJsonRequest;

public class LoginActivity extends CommonActivity {

    private EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setHeaderTitle(getString(R.string.login_now));

        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    // attempt login with required validation
    public void login() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
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
        if (TextUtils.isEmpty(password)) {
            common.setToastMessage(getString(R.string.valid_required_password));
            focusView = editPassword;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            HashMap<String, String> params = new HashMap<>();


            params.put("user_email", email);
            params.put("user_password", password);

            // this class for handle request response thread and return response data
            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.LOGIN_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {

                            JSONObject userdata = null;
                            try {
                                userdata = new JSONObject(responce);

                                if (userdata.has("is_email_varified")
                                        && userdata.getString("is_email_varified").equalsIgnoreCase("0")) {
                                    Intent verifyIntent = new Intent(LoginActivity.this, SendVerificationActivity.class);
                                    verifyIntent.putExtra("user_id", userdata.getString("user_id"));
                                    verifyIntent.putExtra("user_email", userdata.getString("user_email"));
                                    startActivity(verifyIntent);
                                } else {
                                    common.setSession(ApiParams.COMMON_KEY, userdata.getString("user_id"));
                                    common.setSession(ApiParams.USER_FULLNAME, userdata.getString("user_fullname"));
                                    common.setSession(ApiParams.USER_EMAIL, userdata.getString("user_email"));
                                    common.setSession(ApiParams.USER_PHONE, userdata.getString("user_phone"));

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

    // attempt forgot password with required validation
    public void forgotPassword(View view) {
        String email = editEmail.getText().toString();

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
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_email", email);

            // this class for handle request response thread and return response data
            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.FORGOT_PASSWORD_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            common.setToastMessage(responce);
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

    // register button click event
    public void registerClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

}
