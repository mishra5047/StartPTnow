package adapters;

import android.app.Activity;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.doctappo.R;

import java.util.ArrayList;

import models.ServicesModel;

public class ServiceChargeAdapter extends RecyclerView.Adapter<ServiceChargeAdapter.ProductHolder> {
    //private Context context;
    private Activity activity;
    private ArrayList<ServicesModel> postItems;
    int count = 0;

    public ServiceChargeAdapter(Activity context, ArrayList<ServicesModel> arraylist) {
        this.activity = context;
        postItems = arraylist;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_charge, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final ServicesModel categoryModel = postItems.get(position);
        String stitle = categoryModel.getService_title();
        if (!categoryModel.getService_discount().equalsIgnoreCase("0")) {
            stitle = stitle + " ( Off :" + categoryModel.getService_discount() + "%)";
            holder.txtPriceAct.setText(String.format("%.2f", Double.parseDouble(categoryModel.getService_price())));
            holder.txtPriceAct.setPaintFlags(holder.txtPriceAct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Double getAmt = Double.parseDouble(categoryModel.getService_price());
            Double detDisc = Double.parseDouble(categoryModel.getService_discount());
            Double finalAmt = getAmt - (detDisc * getAmt / 100);
        }
        holder.chbox.setText(stitle);
        holder.chbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                categoryModel.setChecked(holder.chbox.isChecked());
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChanged();
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView txtPrice;
        TextView txtPriceAct;
        CheckBox chbox;

        public ProductHolder(View itemView) {
            super(itemView);
            txtPrice = (TextView) itemView.findViewById(R.id.textView1);
            chbox = (CheckBox) itemView.findViewById(R.id.checkBox1);
        }
    }


    public interface OnDataChangeListener {
        public void onDataChanged();
    }

    OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

}

