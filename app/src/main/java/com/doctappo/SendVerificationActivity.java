package com.doctappo;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import Config.ApiParams;
import util.VJsonRequest;

public class SendVerificationActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_verification);
        getSupportActionBar().hide();

        TextView tv_title = (TextView) findViewById(R.id.tv_send_verification_title);
        TextView btn_back = (TextView) findViewById(R.id.btn_send_verification_back);
        Button btn_resend = (Button) findViewById(R.id.btn_send_verification_resend);

        String msg = getResources().getString(R.string.your_registered_email) + " <font color='#3dcdad'>" +
                getIntent().getStringExtra("user_email")
                + "</font> " + getResources().getString(R.string.is_still_not_verified_please_click_on_verification_link_sent_to_your_email_you_may_check_in_inbox_or_spam_folder);

        String styledText = "This is <font color='red'>simple</font>.";
        tv_title.setText(Html.fromHtml(msg), TextView.BufferType.SPANNABLE);

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeResendCode();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        makeResendCode();

    }

    private void makeResendCode() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wail...");
        progressDialog.show();


        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", getIntent().getStringExtra("user_id"));

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.SEND_VERIFY_CODE_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        progressDialog.dismiss();
                        common.setToastMessage(responce);
                    }

                    @Override
                    public void VError(String responce) {
                        progressDialog.dismiss();
                        common.setToastMessage(responce);
                    }
                });

    }

}
