package com.example.romeltined.myrewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTvResult;
    private TextView textToken;
    private String token;

    private ListView mainListView;
    private VoucherArrayAdapter listAdapter;
    private Button spend;
    private Context context;
    private  ArrayList<Voucher> voucherList = new ArrayList<Voucher>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        spend = (Button) findViewById(R.id.btnSpend);
        // Find the ListView resource.

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        token= preferences.getString("token", null);

        mainListView = (ListView) findViewById(R.id.mainListView);
        listAdapter = new VoucherArrayAdapter(getBaseContext(), voucherList);
        mainListView.setAdapter(listAdapter);

        mainListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item,
                                            int position, long id) {
                        Voucher voucher = listAdapter.getItem(position);
                        voucher.toggleChecked();
                        VoucherViewHolder viewHolder = (VoucherViewHolder) item
                                .getTag();
                        viewHolder.getCheckBox().setChecked(voucher.isChecked());
                    }
                });

        GetVoucher();

        Button btnSpend = (Button) findViewById(R.id.btnSpend);
        btnSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
                for (int i = 0; i < listAdapter.getCount(); i++)
                {
                    Voucher voucher = listAdapter.getItem(i);
                    int checker = voucher.id;
                    if (voucher.isChecked())
                    {
                        voucherList.add(voucher);
                    }
                }

                MerchantStaff merchantStaff = new MerchantStaff("bfa1c049-7fb9-4a6e-a371-96f3d80df9d3", "puregold","memel" );
                VoucherSpend voucherSpend = new VoucherSpend();
                voucherSpend.MerchantStaff=merchantStaff;
                voucherSpend.VoucherList=voucherList;
                VoucherBaseLog voucherBaseLog=new VoucherBaseLog();
                voucherBaseLog.Id=0;
                voucherBaseLog.UserId=0;
                voucherBaseLog.CreatedOn=null;

                Gson gson = new Gson();
                String json = gson.toJson(voucherSpend);
                voucherBaseLog.Content="["+json+"]";

                SpendVoucher(voucherBaseLog);
                finish();
                startActivity(getIntent());
            }
        });

        Button btnTransfer = (Button) findViewById(R.id.btnTransfer);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
                for (int i = 0; i < listAdapter.getCount(); i++)
                {
                    Voucher voucher = listAdapter.getItem(i);
                    int checker = voucher.id;
                    if (voucher.isChecked())
                    {
                        voucherList.add(voucher);
                    }
                }
                EditText receiver = (EditText)findViewById(R.id.txtReceiver);
                VoucherTransfer voucherTransfer = new VoucherTransfer();
                voucherTransfer.ReceiverUserName=receiver.getText().toString();
                voucherTransfer.VoucherList=voucherList;
                VoucherBaseLog voucherBaseLog=new VoucherBaseLog();
                voucherBaseLog.Id=0;
                voucherBaseLog.UserId=0;
                voucherBaseLog.CreatedOn=null;

                Gson gson = new Gson();
                String json = gson.toJson(voucherTransfer);
                voucherBaseLog.Content="["+json+"]";

                TransferVoucher(voucherBaseLog);
                finish();
                startActivity(getIntent());
            }
        });
    }


    protected void TransferVoucher(VoucherBaseLog voucherBaseLog){
        String url = "http://192.168.1.200/api/VoucherTransferLogsApi/";

        Gson gson = new Gson();
        String json = gson.toJson(voucherBaseLog);

        JSONObject voucherBaseLogJsonObj = new JSONObject();

        try {
            voucherBaseLogJsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest myJsonRequest = new JsonObjectRequest(Request.Method.POST,
                url, voucherBaseLogJsonObj , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jObj) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        }
                ;
        Volley.newRequestQueue(getBaseContext()).add(myJsonRequest);

    }

    protected void SpendVoucher(VoucherBaseLog voucherBaseLog){
        String url = "http://192.168.1.200/api/VoucherSpendLogsApi/";

        Gson gson = new Gson();
        String json = gson.toJson(voucherBaseLog);

        JSONObject voucherBaseLogJsonObj = new JSONObject();

        try {
            voucherBaseLogJsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest myJsonRequest = new JsonObjectRequest(Request.Method.POST,
                url, voucherBaseLogJsonObj , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jObj) {
                String x = jObj.toString();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                }
            }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        }
                ;
        Volley.newRequestQueue(getBaseContext()).add(myJsonRequest);

    }

    protected void GetVoucher(){
        String url = "http://192.168.1.200/api/VoucherApi/";
        boolean result = false;

        JsonArrayRequest jsonObjRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //String jsonResponse;
                        try {
                            String jsonResponse;
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response
                                        .get(i);
                                String voucherType = item.getString("VoucherType");
                                JSONObject jsonObject = new JSONObject(voucherType);
                                String vType = jsonObject.get("Name").toString();

                                voucherList.add(new Voucher(Integer.parseInt(item.getString("Id")),vType));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }

        })
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        };
        Volley.newRequestQueue(getBaseContext()).add(jsonObjRequest);

    }

    /** Holds child views for one row. */
    private static class VoucherViewHolder
    {
        private CheckBox checkBox;
        private TextView textId;
        private TextView textName;

        public VoucherViewHolder()
        {
        }

        public VoucherViewHolder( CheckBox checkBox, TextView textId, TextView textName)
        {
            this.checkBox = checkBox;
            this.textId = textId;
            this.textName = textName;
        }

        public CheckBox getCheckBox()
        {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }

        public TextView getTextName()
        {
            return textName;
        }

        public TextView getTextId()
        {
            return textId;
        }

        public void setTextName(TextView textName)
        {
            this.textName = textName;
        }
    }

    private static class VoucherArrayAdapter extends ArrayAdapter<Voucher>
    {
        private LayoutInflater inflater;
        private ArrayList<Voucher> voucherList;

        public VoucherArrayAdapter(Context context, ArrayList<Voucher> voucherList)
        {
            super(context, R.layout.voucherrow, R.id.rowTextId, voucherList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);
            voucherList=voucherList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Planet to display
            Voucher voucher = (Voucher) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox;
            TextView textId;
            TextView textName;

            // Create a new row view
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.voucherrow, null);

                // Find the child views.
                textId = (TextView) convertView
                        .findViewById(R.id.rowTextId);
                checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
                textName = (TextView) convertView.findViewById(R.id.rowTextName);

                // Optimization: Tag the row with it's child views, so we don't
                // have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new VoucherViewHolder(checkBox, textId, textName));

                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v;
                        Voucher voucher = (Voucher) cb.getTag();
                        voucher.setChecked(cb.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else
            {
                // Because we use a ViewHolder, we avoid having to call
                // findViewById().
                VoucherViewHolder viewHolder = (VoucherViewHolder) convertView
                        .getTag();
                checkBox = viewHolder.getCheckBox();
                textId = viewHolder.getTextId();
                textName = viewHolder.getTextName();
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(voucher);

            // Display planet data
            checkBox.setChecked(voucher.isChecked());
            textId.setText(String.valueOf(voucher.getId()));
            textName.setText(voucher.getName());

            return convertView;
        }

    }
}
