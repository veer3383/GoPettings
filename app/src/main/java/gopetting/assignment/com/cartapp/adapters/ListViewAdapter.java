package gopetting.assignment.com.cartapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import gopetting.assignment.com.cartapp.R;
import gopetting.assignment.com.cartapp.model.Product;

/**
 * Created by viraj on 4/6/16.
 */

public class ListViewAdapter extends ArrayAdapter<Product> {
    public ListViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        Product product = getItem(position);
        final ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
        Ion.with(getContext()).load(product.getImageId()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (e == null) {
                    img.setImageBitmap(result);
                } else {
                    img.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });
        txtTitle.setText(product.getTitle());
        txtDescription.setText(product.getDescription());

        return v;
    }
}
