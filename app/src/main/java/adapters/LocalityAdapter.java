package adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doctappo.R;

import java.util.ArrayList;

import models.LocalityModel;


/**
 * Created by Light link on 04/07/2016.
 */
public class LocalityAdapter extends RecyclerView.Adapter<LocalityAdapter.ProductHolder> {

    private ArrayList<LocalityModel> list;
    private Activity activity;

    public LocalityAdapter(Activity activity, ArrayList<LocalityModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_locality, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final LocalityModel categoryModel = list.get(position);


        holder.lbl_title.setText(categoryModel.getLocality() + "," + categoryModel.getCity_name() + "," + categoryModel.getCountry_name());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("locality", categoryModel.getLocality());
                b.putString("latitude", categoryModel.getLocality_lat());
                b.putString("longitude", categoryModel.getLocality_lon());
                b.putString("locality_id", categoryModel.getLocality_id());
                Intent intent = new Intent();
                intent.putExtras(b);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView lbl_title;

        public ProductHolder(View itemView) {
            super(itemView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
        }
    }


}
