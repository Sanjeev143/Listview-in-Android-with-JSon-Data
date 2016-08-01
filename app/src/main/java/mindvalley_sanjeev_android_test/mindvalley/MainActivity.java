package mindvalley_sanjeev_android_test.mindvalley;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mindvalley_sanjeev_android_test.mindvalley.Services.Constant;
import mindvalley_sanjeev_android_test.mindvalley.Services.MyAdapter;
import mindvalley_sanjeev_android_test.mindvalley.Services.ServiceHandler;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private static String username;
    private static String profile_image;
    private static int likes;
    RecyclerView recyclerView;
    MyAdapter adapter;
    GetContacts getContactsThread;
    // Hashmap for ListView
    static ArrayList<HashMap<String, String>> user_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);


        user_List = new ArrayList<HashMap<String, String>>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        checkInternetConenction();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(getContactsThread==null) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getContactsThread = new GetContacts();
                                            getContactsThread.execute();
                                        }
                                    }
                                }
        );
    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constant.url, ServiceHandler.GET);

            HashMap<String, String> user_detail;
            // user_List = new ArrayList<HashMap<String, String>>();
            if (jsonStr != null) {
                try {

                    JSONArray jsonarray = new JSONArray(jsonStr);
                    // looping through All Contacts
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        try {
                            likes = c.getInt(Constant.LIKES);
                            JSONObject user = c.getJSONObject(Constant.TAG_CONTACTS);
                            username = user.getString(Constant.USERNAME);

                            JSONObject imgurl = user.getJSONObject(Constant.PROFILE_IMAGE);
                            profile_image = imgurl.getString(Constant.IMAGE);

                            // adding each child node to HashMap key => value
                            user_detail = new HashMap<String, String>();
                            user_detail.put(Constant.LIKES, Integer.toString(likes));
                            user_detail.put(Constant.USERNAME,username);
                            user_detail.put(Constant.IMAGE, profile_image);
                            user_List.add(user_detail);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /**
             * Updating parsed JSON data into ListView
             * */
//            adapter.notifyDataSetChanged();
            adapter = new MyAdapter(user_List, getApplicationContext());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            getContactsThread = null;
        }
    }
    /**
            * This method is called when swipe refresh is pulled down
    */
    @Override
    public void onRefresh() {

        user_List.clear();
        if(getContactsThread==null) {
            swipeRefreshLayout.setRefreshing(true);
            getContactsThread = new GetContacts();
            getContactsThread.execute();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        /**
         * Calling for clear cache......
         * */
        clearApplicationData();
    }
    /**
     * Fuction for clear cache......
     * */
    public void clearApplicationData()
    {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Please connect to the Internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), "   "+message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
          //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
           // showSnack(true);
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
         //   Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            showSnack(false);
            return false;
        }
        return false;
    }

}
