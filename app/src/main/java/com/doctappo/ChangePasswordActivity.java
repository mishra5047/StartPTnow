package com.doctappo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.HashMap;

import Config.ApiParams;

import util.VJsonRequest;

public class ChangePasswordActivity extends CommonActivity {

    private EditText txtNewPass, txtCPass, txtRPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        allowBack();
        setHeaderTitle(getString(R.string.change_password));

        txtNewPass = (EditText) findViewById(R.id.txtNewPassword);
        txtCPass = (EditText) findViewById(R.id.txtCurrentPassword);
        txtRPass = (EditText) findViewById(R.id.txtRePassword);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    // check validation on changepassword
    public void register() {

        String newPassword = txtNewPass.getText().toString();
        String currentPassword = txtCPass.getText().toString();
        String rePassword = txtRPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(currentPassword)) {
            common.setToastMessage(getString(R.string.valid_required_current_password));
            focusView = txtCPass;
            cancel = true;
        }
        if (TextUtils.isEmpty(newPassword)) {
            common.setToastMessage(getString(R.string.valid_required_new_password));
            focusView = txtNewPass;
            cancel = true;
        }
        if (!txtNewPass.getText().equals(txtRPass.getText())) {
            common.setToastMessage(getString(R.string.valid_not_match));
            focusView = txtRPass;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("c_password", currentPassword);
            params.put("n_password", newPassword);
            params.put("r_password", rePassword);
            params.put("user_id", common.get_user_id());

            // this class for handle request response thread and return response data
            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.CHANGE_PASSWORD_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            common.setToastMessage(responce);
                            finish();

                        }

                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });
        }

    }

}
