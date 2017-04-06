package gopetting.assignment.com.cartapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gopetting.assignment.com.cartapp.adapters.GridViewAdapter;
import gopetting.assignment.com.cartapp.adapters.ListViewAdapter;
import gopetting.assignment.com.cartapp.model.Product;
import gopetting.assignment.com.cartapp.utilities.CartCountDisplay;
import gopetting.assignment.com.cartapp.utilities.UserSession;

/**
 * Created by viraj on 4/6/16.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.stub_grid)
    ViewStub stubGrid;
    @Bind(R.id.stub_list)
    ViewStub stubList;
    ListView listView;
    GridView gridView;
    @Bind(R.id.switchview)
    Button switchbtn;
    private List<Product> productList;
    UserSession session;
    String currentViewMode = "list";


    static final String VIEW_MODE_LISTVIEW = "list";
    static final String VIEW_MODE_GRIDVIEW = "grid";

    CartCountDisplay ccd;
    String cart,listjson;

    ArrayList<HashMap<String, String>> prodlist;
    HashMap<String, String> prodmap;
    JSONArray prodjson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        session = new UserSession(getApplicationContext());
        HashMap<String, String> response = session.getViewMode();
        currentViewMode = response.get(UserSession.MODE);

        HashMap<String, String> httpresponse = session.getJSON();
        listjson = httpresponse.get(UserSession.RESPONSE);
        Log.v("resp",listjson);

        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });
        generatelist();
        switchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VIEW_MODE_LISTVIEW.equals(currentViewMode)) {
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                switchView();
                session.StoreviewMode(currentViewMode);
            }
        });

        stubList.inflate();
        stubGrid.inflate();
        listView = (ListView) findViewById(R.id.items_inlist);
        gridView = (GridView) findViewById(R.id.items_ingrid);

        prodlist = new ArrayList<>();
        prodmap = new HashMap<>();

        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);
        switchView();

    }

    private void generatelist() {
        try {
            JSONObject obj = new JSONObject(listjson);
            JSONArray jsonMainNode = obj.optJSONArray("data");
            if (jsonMainNode != null) {
                productList = new ArrayList<>();
                for(int i=0; i < jsonMainNode.length(); i++)
                {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    productList.add(new Product(jsonChildNode.getString("icon"),jsonChildNode.getString("name"),jsonChildNode.getString("endDate")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void switchView() {

        if(VIEW_MODE_LISTVIEW.equals(currentViewMode)) {
            switchbtn.setText("Switch to Gridview");
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);
        } else {
            switchbtn.setText("Switch to listview");
            stubList.setVisibility(View.GONE);
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();


    }

    private void setAdapters() {
        if(VIEW_MODE_LISTVIEW.equals(currentViewMode)) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(this, R.layout.list_item, productList);
            listView.setAdapter(listViewAdapter);
        } else {
            GridViewAdapter gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, productList);
            gridView.setAdapter(gridViewAdapter);
        }
    }
    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HashMap<String, String> response = session.getCartCount();
            String getcartcount = response.get(UserSession.CARTCOUNT);
            if (getcartcount == null) {
                getcartcount = "0";
            }
            int newCount = Integer.parseInt(getcartcount);
            prodmap = new HashMap<>();
            prodmap.put("icon", productList.get(position).getImageId());
            prodmap.put("name", productList.get(position).getTitle());
            prodmap.put("endDate", productList.get(position).getDescription());
            prodlist.add(prodmap);

            prodjson = new JSONArray(prodlist);
            session.Storecart(prodjson.toString());
            newCount++;
            ccd = new CartCountDisplay();
            ccd.displaycartCount(newCount, (TextView) findViewById(R.id.cartCount), session);
            ccd.animateCart((ImageView) findViewById(R.id.cart), (TextView) findViewById(R.id.cartCount));        }
    };

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> response = session.getCartItems();
        cart = response.get(UserSession.CART);
        if (cart != null && !cart.isEmpty() && !cart.equals("null")) {

            prodlist = new ArrayList<>();
            try {
                prodjson = new JSONArray(cart);
                int count = prodjson.length();
                for (int j = 0; j < count; j++) {
                    JSONObject jsonobj = prodjson.getJSONObject(j);
                    prodmap = new HashMap<>();
                    prodmap.put("icon", jsonobj.getString("icon"));
                    prodmap.put("name", jsonobj.getString("name"));
                    prodmap.put("endDate", jsonobj.getString("endDate"));
                    prodlist.add(prodmap);
                }
                ccd = new CartCountDisplay();
                ccd.displaycartCount(count, (TextView) findViewById(R.id.cartCount), session);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
