package dialogues;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;


import com.doctappo.MainActivity;
import com.doctappo.R;

import java.util.Locale;

import Config.ConstValue;
import util.CommonClass;


/**
 * Created by Dell on 19-11-2016.
 */

public class LanguagePrfsDialog extends Dialog {

    private Activity context;
    private Button btnEnglish, btnArabic;
    private SharedPreferences sharedPreferences;

    public LanguagePrfsDialog(final Activity context) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.dialog_language_selection);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } catch (Exception ex) {

        }

        this.btnEnglish = (Button) this.findViewById(R.id.btnEnglish);
        this.btnArabic = (Button) this.findViewById(R.id.btnArabic);
        this.btnEnglish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {

                saveLanguage("en");
                CommonClass.initRTL(context, "en");
                LanguagePrfsDialog.this.hide();
                dismiss();
                /*Intent i1 = new Intent(context, MainActivity.class);
                context.startActivity(i1);
                context.finish();*/
                ((Activity) context).recreate();
            }
        });

        this.btnArabic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                saveLanguage("es");
                CommonClass.initRTL(context, "es");
                LanguagePrfsDialog.this.hide();
                dismiss();
                /*Intent i1 = new Intent(context, MainActivity.class);
                context.startActivity(i1);
                context.finish();*/
                ((Activity) context).recreate();
            }
        });
    }

    // save language data in session
    public void saveLanguage(String language) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ConstValue.PREFS_LANGUAGE, language);


            editor.apply();
            language = language.equalsIgnoreCase("es") ? "Spanish" : "English";
            //Toast.makeText(context, language + " is your preferred language.", Toast.LENGTH_SHORT).show();
        } catch (Exception exc) {

        }

    }

    public void dismiss() {
        super.dismiss();
    }
}
