package adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.doctappo.R;

import java.util.ArrayList;

import models.BulletTextModel;


/**
 * Created by Light link on 04/07/2016.
 */
public class BulletTextAdapter extends RecyclerView.Adapter<BulletTextAdapter.ProductHolder> {

    private ArrayList<BulletTextModel> list;
    private Activity activity;

    public BulletTextAdapter(Activity activity, ArrayList<BulletTextModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bullet_text, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final BulletTextModel mList = list.get(position);

        holder.tv_text.setText(Html.fromHtml(mList.getText()));
        if (!mList.getLink().isEmpty()
                && (URLUtil.isValidUrl(mList.getLink())
                || Patterns.WEB_URL.matcher(mList.getLink()).matches())) {
            holder.tv_text.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tv_text.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public ProductHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_bullet_text);

            tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    BulletTextModel bulletTextModel = list.get(position);

                    if (!bulletTextModel.getLink().isEmpty()
                            && (URLUtil.isValidUrl(bulletTextModel.getLink())
                            || Patterns.WEB_URL.matcher(bulletTextModel.getLink()).matches())) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bulletTextModel.getLink()));
                        activity.startActivity(intent);
                    }
                }
            });

        }
    }


}
