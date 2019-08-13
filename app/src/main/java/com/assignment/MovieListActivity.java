package com.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity implements RecycleItemListener {




    private SearchView searchView;
    private RecyclerView recyclerView;
    private MovieItem movieItem;
    private List<Movie> movieList = new ArrayList<>();
    private RequestQueue queue ;
    private ProgressDialog pd;

    @Override
    public void onClickItem(String imgID) {
        Intent intent = new Intent(getApplicationContext(),MovieDetailsActivity.class);
        intent.putExtra("imgID",imgID);
        startActivity(intent);
    }
//onCreate method...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = (SearchView) findViewById(R.id.sv);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieItem = new MovieItem(this,this,movieList);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(myLayoutManager);
        queue = Volley.newRequestQueue(this);
        setTitle("Movie List");
        recyclerView.setAdapter(movieItem);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(Utils.isNetworkConnected(MovieListActivity.this))
                        searchCondidate(query);
                else
                    Toast.makeText(getApplicationContext(),"Internet is not connected!",Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               //searchCondidate(newText);
                return false;
            }
        });



    }


    private void searchCondidate(String text){

        pd = Utils.createProgress(this);
        String url = String.format(GlobalConstant.OMDBAPI_SEARCH_URL,text );
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("Response");
                    if("False".equalsIgnoreCase(error)){
                        Toast.makeText(MovieListActivity.this,jsonObject.getString("Error"),Toast.LENGTH_LONG).show();
                    }else{
                        movieList.clear();
                        //tvCount.setText(""+jsonObject.getInt("count")+" Records found.");
                        JSONArray result = jsonObject.getJSONArray("Search");
                        for (int i= 0; i<result.length();i++){
                            JSONObject item = result.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setTitle(item.getString("Title"));
                            movie.setYear(item.getString("Year"));
                            movie.setImageUrl(item.getString("Poster"));
                            movie.setImdbID(item.getString("imdbID"));
                            movie.setType(item.getString("Type"));
                            movieList.add(movie);
                        }
                        movieItem.notifyDataSetChanged();
                   }
                }catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MovieListActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pd.dismiss();
                Toast.makeText(MovieListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(req);
        pd.show();
    }





}
