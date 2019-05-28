package dschik.noticeboard;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NoticeAsyncTask extends AsyncTask<URL, String, String>{
    ArrayList<DataObject> dt;
    Context c;
    RecyclerView mrecyler;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    ShimmerFrameLayout sh;
    SwipeRefreshLayout swip;
    NoticeAsyncTask(Context context, RecyclerView recyclerView, ShimmerFrameLayout sh1,SwipeRefreshLayout ss)
    {
        mrecyler = recyclerView;
        c = context;
        sh = sh1;
        swip =ss;
    }

    @Override
    protected String doInBackground(URL... s) {

        String json = "";
        try{
            HttpURLConnection connection = (HttpURLConnection) s[0].openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            json = br.readLine();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return json;

    }

    @Override
    protected void onPostExecute(String s)
    {
        JSONObject jobj= null;

        sh.stopShimmer();
        swip.setRefreshing(false);
        sh.setVisibility(View.GONE);
        Log.d("aa","called");
        try {
            int i =0;
            dt = new ArrayList<>();
            //jobj = new JSONObject(s);
            JSONArray jarray = new JSONArray(s);
            int size = jarray.length();
            //Log.d("aa","test"+size);
            for (i=0; i< size; i++) {
                JSONObject j = jarray.getJSONObject(i);
                String head = j.getString("title");
                String link = "http://heritageit.edu/"+j.getString("link");
                DataObject obj = new DataObject(head, link);
                dt.add(i, obj);
                //Log.d("aa","*--");
                myRecyclerViewAdapter= new MyRecyclerViewAdapter(dt);
                mrecyler.setAdapter(myRecyclerViewAdapter);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
