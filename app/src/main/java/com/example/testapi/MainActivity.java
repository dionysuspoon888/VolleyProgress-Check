package com.example.testapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //DATE TRANSMITTED KEY
    public static final String EXTRA_link = "link";
    public static final String EXTRA_displayLink = "displayLink";

    private Context ctx;

    //Volley for JSON
    public RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private JsonObjectRequest request;
    JSONArray jsonArray;
    public int progressCount;
    boolean progressFin = false;
    public int i;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        parseJSON();

        //External Checking Volley Finished & initView
        new Handler().postDelayed(checkingFinish,100);




    }

    private void parseJSON() {
        Log.i("Json","SS");
        progressCount = 0;

        //JSON Link,q for search,type=photo
        String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBWieNyVmRNq46Kmy-1rgEU__-I-MVP6SM&cx=005254515861270210630:17risigovei&q=ads&searchType=image";
        //JSONRequest for later use
          request = new JsonObjectRequest(Request.Method.GET, url, null,





                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //array that stores all the object in API Documentation
                             jsonArray = response.getJSONArray("items");
                            Log.i("JsonArray","SS");
                            //Loop all the object of items array
                            for (i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                Log.i("JsonItem","SS");

                                //Extrieve what we want by Keys

                                String creatorName = item.getString(EXTRA_displayLink);
                                String imageUrl = item.getString(EXTRA_link);


                                Log.i("JsonValue","Value: "+imageUrl);

                                GlobalConstants.Jlist.add(new JSONItem(imageUrl, creatorName));

                                JSONItem it= GlobalConstants.Jlist.get(i);
                                Log.i("JsonValue added?","Value: "+it.getCreator());
                               // Log.e("What is it","getBody"+request.getBody());
                                progressing(i);

                            }


                            Log.i("initView","SS");




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
        progress();





    }

    public void initView(){
        RecyclerView recyclerView = findViewById(R.id.rv_1);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        Radapter radapter = new Radapter(MainActivity.this,GlobalConstants.Jlist);
        recyclerView.setAdapter(radapter);
        Log.i("initViewFinish","SS");
    }

    public void progress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Hi auntie");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);

        //progressDialog.setProgress(10); not run cuz Main Thread by MainActivity running=>Thread/Runnable/Handler
        progressDialog.show();
    }


     public void progressing(int i) {
         Log.e("i is "," i : "+i+ "jsonArray Length "+jsonArray.length());
         if(jsonArray != null) {
             if(i < jsonArray.length() - 1) {
                 progressDialog.setProgress(100 / jsonArray.length() * i);
             }else{
                 progressFin = true;
                 progressDialog.setProgress(100);


             }
         }
     }


    public Runnable checkingFinish = new Runnable() {

            @Override
            public void run() {
                Log.e("Run","Where is progresCount"+progressCount);

                    if (progressFin) {
                            Log.i("initView","SS ");

                            initView();
                        progressDialog.dismiss();

                    }else{
                        Log.e("Hard Working checking ","SS CHECKING");
                        new Handler().postDelayed(checkingFinish,500);

                    }

            }

    };
    //progress not update cuz
//    当前线程是出于MainActivity.java，属于主线程，UI线程；
//
//    所以，之前每次虽然调用setProgress，但是此时线程还是出于忙的状态，所以UI中的进度条没法更新。
//
//    需要用到另外一个Tread或者Hander，去处理进度条更新的问题。
}
