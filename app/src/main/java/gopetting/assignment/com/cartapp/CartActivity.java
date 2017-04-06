package gopetting.assignment.com.cartapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import gopetting.assignment.com.cartapp.utilities.CartCountDisplay;
import gopetting.assignment.com.cartapp.utilities.UserSession;

/**
 * Created by viraj on 4/6/16.
 */
public class CartActivity extends AppCompatActivity {
    UserSession session;
    CartListAdapter adapter;
    CartCountDisplay ccd;
    ArrayList<HashMap<String, String>> arraylist;
    HashMap<String, String> map;
    JSONArray jsarray;
    @Bind(R.id.layer1main)
    RelativeLayout l1;
    @Bind(R.id.layer2main)
    RelativeLayout l2;
    String cart;
    @Bind(R.id.list)
    ListView cartlist;

    Toolbar mToolbar;

    public static String CNAME = "name";
    public static String CICON = "icon";
    public static String CDATE = "endDate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        ButterKnife.bind(this);
        session = new UserSession(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HashMap<String, String> response = session.getCartItems();
        cart = response.get(UserSession.CART);

        if (cart == null) {
            l2.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);
        }
        if (cart != null && !cart.isEmpty() && !cart.equals("null")) {
            cart = cart.replaceAll("\\\\", "");
            l2.setVisibility(View.GONE);
            l1.setVisibility(View.VISIBLE);

            if (cart.equals("[]")) {
                l2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
            }
            arraylist = new ArrayList<>();
            try {
                JSONArray jsarray = new JSONArray(cart);
                for (int j = 0; j < jsarray.length(); j++) {
                    JSONObject jsonobj;
                    jsonobj = jsarray.getJSONObject(j);
                    map = new HashMap<>();
                    map.put("name", jsonobj.getString("name"));
                    map.put("icon", jsonobj.getString("icon"));
                    map.put("endDate", jsonobj.getString("endDate"));
                    arraylist.add(map);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new CartListAdapter(CartActivity.this, arraylist);
            cartlist.setAdapter(adapter);
            jsarray = new JSONArray(arraylist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    private class CartListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<HashMap<String, String>> data;
        HashMap<String, String> resultp = new HashMap<>();

        CartListAdapter(Context context,
                        ArrayList<HashMap<String, String>> arraylist) {
            this.context = context;
            data = arraylist;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView date, name;
            final ImageView rem, icon;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = inflater.inflate(R.layout.cart_item, parent, false);
            resultp = data.get(position);
            icon = (ImageView) itemView.findViewById(R.id.cart_icon);
            name = (TextView) itemView.findViewById(R.id.cart_name);
            date = (TextView) itemView.findViewById(R.id.cart_date);
            rem =(ImageView) itemView.findViewById(R.id.remove);
            Ion.with(context).load(resultp.get(CICON)).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (e == null) {
                        icon.setImageBitmap(result);
                    } else {
                        icon.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            });
            name.setText(resultp.get(CNAME));
            date.setText(resultp.get(CDATE));
            rem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(position);
                    jsarray = new JSONArray(data);
                    session.Storecart(jsarray.toString());
                    if (jsarray.toString().equals("[]"))
                    {
                        l2.setVisibility(View.VISIBLE);
                        l1.setVisibility(View.GONE);
                    };
                    session.Storecartcount(String.valueOf(jsarray.length()));

                    ccd = new CartCountDisplay();
                    ccd.displaycartCount(jsarray.length(), (TextView) findViewById(R.id.cartCount), session);
                    ccd.animateCart((ImageView) findViewById(R.id.cart), (TextView) findViewById(R.id.cartCount));
                    CartListAdapter.this.notifyDataSetChanged();
                }
            });
            return itemView;
        }

        @Override
        public void notifyDataSetChanged()
        {
            super.notifyDataSetChanged();
        }

    }
}
