package net.usermd.mcichon.quakemodel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.usermd.mcichon.quakemodel.API.EarthquakeAPI;
import net.usermd.mcichon.quakemodel.activities.QuakeDetails;
import net.usermd.mcichon.quakemodel.model.QuakeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<QuakeModel.Features> childrenList;
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/";
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<HashMap<String, String>> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate started");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = new ArrayList<>();
        listView = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "onItemClick started");
                Log.i("ListView", "You clicked Item: " + id + " at position:" + position);

                QuakeModel.Features features = childrenList.get(position);

                Intent intent = new Intent(MainActivity.this, QuakeDetails.class);

                intent.putExtra("title", features.getProperties().getTitle());
                intent.putExtra("place", features.getProperties().getPlace());
                intent.putExtra("time", features.getProperties().getTime());
                intent.putExtra("url", features.getProperties().getUrl());
                intent.putExtra("mag", features.getProperties().getMag());
                intent.putExtra("coordinates", features.getGeometry().getCoordinates());

                startActivity(intent);
            }
        });

        final ListAdapter listAdapter = new SimpleAdapter(
                MainActivity.this,
                result,
                R.layout.activity_list_item,
                new String[] {"title", "place"},
                new int[] {R.id.title, R.id.place}
        );

        listView.setAdapter(listAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        getData();

                        swipeRefreshLayout.setRefreshing(false);
                        listView.smoothScrollToPosition(0);
                    }
                }, 2000);
            }
        });
    }

    private void getData() {

        Log.i(TAG, "getData started");

        result.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EarthquakeAPI redditAPI = retrofit.create(EarthquakeAPI.class);
        Call<QuakeModel> call = redditAPI.getData();
        call.enqueue(new Callback<QuakeModel>() {

            @Override
            public void onResponse(Call<QuakeModel> call, Response<QuakeModel> response) {

                parseJson(response);
            }

            @Override
            public void onFailure(Call<QuakeModel> call, Throwable t) {

                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseJson(final Response<QuakeModel> response) {

        Log.i(TAG, "parseJson started");

        new AsyncTask<Object, Object, String>() {

            @Override
            protected String doInBackground(Object... params) {
                StringBuilder sb = new StringBuilder();
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                Log.d(TAG, "onResponse: received information: " + response.body().toString());

                childrenList = response.body().getFeatures();
                for( int i = 0; i < childrenList.size(); i++ ) {
                    float[] geometry = childrenList.get(i).getGeometry().getCoordinates();

                    sb.append("id: " + childrenList.get(i).getQuake_id() + "\n" +
                            "Title: " + childrenList.get(i).getProperties().getTitle() + "\n" +
                            "Place: " + childrenList.get(i).getProperties().getPlace() + "\n" +
                            "Time: " + childrenList.get(i).getProperties().getTime() + "\n" +
                            "Url: " + childrenList.get(i).getProperties().getUrl() + "\n" +
                            "Mag: " + childrenList.get(i).getProperties().getMag()  + "\n" +
                            "Geometry: " + geometry[0] + " " + geometry[1] + " " + geometry[2] + "\n" +
                            "-------------------------------------------------------------------------\n\n");

                    Log.d(TAG, "onResponse: \n" + sb.toString());

                    HashMap<String, String> repo = new HashMap<>();
                    repo.put("title", childrenList.get(i).getProperties().getTitle());
                    repo.put("place", childrenList.get(i).getProperties().getPlace());
                    result.add(repo);
                }

                return sb.toString();
            }

            @Override
            protected void onPostExecute(String text) {
                super.onPostExecute(text);

                ListAdapter listAdapter = new SimpleAdapter(
                        MainActivity.this,
                        result,
                        R.layout.activity_list_item,
                        new String[] {"title", "place"},
                        new int[] {R.id.title, R.id.place}
                );
                listView.setAdapter(listAdapter);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(TAG, "onCreateOptionsMenu started");

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
