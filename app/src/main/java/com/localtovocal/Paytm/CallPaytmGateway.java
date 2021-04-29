package com.localtovocal.Paytm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CallPaytmGateway extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    //ZdGFec14262945834103
    //XedVWH25576732059833
    String CUST_ID = "", ORDER_ID = "", MID = "XedVWH25576732059833", TXN_AMOUNT = "";
    String checksumUrl = "https://maestros.co.in/Local_to_Vocal/paytm/generateChecksum.php";
    String verifyUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    RequestQueue requestQueue;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Random random = new Random();
        int order = random.nextInt(1000000 + 5);
        int cust_id = random.nextInt(9000000 + 1);

        ORDER_ID = String.valueOf(order);
        CUST_ID = String.valueOf(cust_id);
        TXN_AMOUNT = SharedHelper.getKey(CallPaytmGateway.this, AppConstats.SUBSCRIBE_PLAN_AMOUNT);

        requestQueue = Volley.newRequestQueue(this);

        paytmPayment();


    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " response " + bundle.toString());
        if (bundle.toString().contains("TXN_SUCCESS")) {
            TXN_AMOUNT = SharedHelper.getKey(CallPaytmGateway.this, AppConstats.SUBSCRIBE_PLAN_AMOUNT);
            String userID = SharedHelper.getKey(CallPaytmGateway.this, AppConstats.USER_ID);
            String planID = SharedHelper.getKey(CallPaytmGateway.this, AppConstats.SUBSCRIBE_PLAN_ID);
            String RESPMSG = String.valueOf(bundle.get("RESPMSG"));

            Log.e("uwyewe", RESPMSG);

            book(userID, planID, RESPMSG, TXN_AMOUNT);
        }
    }

    @Override
    public void networkNotAvailable() {
    }

    @Override
    public void clientAuthenticationFailed(String s) {
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " UI fail response  " + s);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading page response " + s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back response  ");
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel ");
    }


    public void paytmPayment() {
        dialog = new ProgressDialog(CallPaytmGateway.this);
        dialog.setMessage("Please wait");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, checksumUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sdnskjfn", "fjdjkf" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String checksum = jsonObject.getString("CHECKSUMHASH");

                    Log.e("xmmxkxs", "fjdjkf" + checksum);

                    PaytmPGService Service = PaytmPGService.getStagingService("");
                    HashMap<String, String> paramMap = new HashMap<>();

                    paramMap.put("MID", MID);
                    paramMap.put("ORDER_ID", ORDER_ID);
                    paramMap.put("CUST_ID", CUST_ID);
                    paramMap.put("CHANNEL_ID", "WAP");
                    paramMap.put("TXN_AMOUNT", TXN_AMOUNT);
                    paramMap.put("WEBSITE", "WEBSTAGING");
                    paramMap.put("CALLBACK_URL", verifyUrl);
                    paramMap.put("CHECKSUMHASH", checksum);
                    paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                    PaytmOrder Order = new PaytmOrder(paramMap);
                    Log.e("checksum", paramMap.toString());
                    Service.initialize(Order, null);

                    dialog.hide();

                    Service.startPaymentTransaction(CallPaytmGateway.this, true, true,
                            CallPaytmGateway.this);
                    finish();


                } catch (Exception e) {
                    Log.e("error", "fjdjkf" + e.getMessage(), e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("MID", MID);
                param.put("CUST_ID", CUST_ID);
                param.put("ORDER_ID", ORDER_ID);
                param.put("TXN_AMOUNT", TXN_AMOUNT);
                param.put("CHANNEL_ID", "WAP");
                param.put("WEBSITE", "WEBSTAGING");
                param.put("CALLBACK_URL", verifyUrl);
                param.put("INDUSTRY_TYPE_ID", "Retail");
                return param;
            }
        };

        requestQueue.add(request);


    }


    public void book(String userID, String planID, String txn_id, String amount) {

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, response -> {

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String user_planID = object.getString("user_planID");
                    Toast.makeText(CallPaytmGateway.this, "Success", Toast.LENGTH_SHORT).show();

                    Log.e("xjsbhjsh", "fjdjkf" + user_planID);


                } catch (Exception e) {
                    Log.e("shuief", "fjdjkf" + e.getMessage(), e);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("dsjlkdjsk", "fjdjkf" + error.getMessage(), error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("control", "add_plan");
                param.put("userID", userID);
                param.put("planID", planID);
                param.put("txn_id", txn_id);
                param.put("amount", amount);
                return param;
            }
        };

        requestQueue.add(request);
    }


}