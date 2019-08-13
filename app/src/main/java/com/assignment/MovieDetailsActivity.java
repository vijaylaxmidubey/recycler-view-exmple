package com.assignment;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView tvDescreption, tvYear, tvName;
    private RatingBar rb;
    private ImageView image;
    private RequestQueue queue ;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        tvDescreption=(TextView)findViewById(R.id.tvDescreption);
        tvYear =(TextView)findViewById(R.id.tvYear);
        tvName =(TextView)findViewById(R.id.tvName);
        image =(ImageView) findViewById(R.id.image);
        rb=(RatingBar)findViewById(R.id.rb);
        queue = Volley.newRequestQueue(this);
        setTitle("Movie Details");
        System.out.println("Image ID "+ getIntent().getExtras().getString("imgID"));
        if(Utils.isNetworkConnected(MovieDetailsActivity.this))
            fetchMovieDetails(getIntent().getExtras().getString("imgID"));
        else
            Toast.makeText(getApplicationContext(),"Internet is not connected!",Toast.LENGTH_LONG).show();


    }

    private void fetchMovieDetails(String imgId){

        pd = Utils.createProgress(this);
        String url = String.format(GlobalConstant.OMDBAPI_IMG_TAG_URL,imgId );
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("Response");
                    if("False".equalsIgnoreCase(error)){
                        Toast.makeText(MovieDetailsActivity.this,jsonObject.getString("Error"),Toast.LENGTH_LONG).show();
                    }else{
                        tvName.setText(jsonObject.getString("Title"));
                        tvYear.setText(jsonObject.getString("Year"));
                        StringBuilder sb = new StringBuilder();
                        sb.append("<b>Released : </b>%s<br><br>");
                        sb.append("<b>Runtime  : </b>%s<br><br>");
                        sb.append("<b>Genre  : </b>%s<br><br>");
                        sb.append("<b>Director  : </b>%s<br><br>");
                        sb.append("<b>Writer  : </b>%s<br><br>");
                        sb.append("<b>Actors  : </b>%s<br><br>");
                        sb.append("<b>Plot  : </b>%s<br><br>");
                        sb.append("<b>Language  : </b>%s<br><br>");
                        sb.append("<b>Country  : </b>%s<br><br>");
                        sb.append("<b>Awards  : </b>%s<br><br>");
                        sb.append("<b>Metascore  : </b>%s<br><br>");
                        sb.append("<b>Votes  : </b>%s<br><br>");
                        sb.append("<b>Type  : </b>%s<br><br>");
                        sb.append("<b>BoxOffice  : </b>%s<br><br>");
                        sb.append("<b>Production  : </b>%s<br><br>");
                        sb.append("<b>Website  : </b>%s<br><br>");
                        String finalResult = String.format(sb.toString(),
                                jsonObject.getString("Released"),
                                jsonObject.getString("Runtime"),
                                jsonObject.getString("Genre"),
                                jsonObject.getString("Director"),
                                jsonObject.getString("Writer"),
                                jsonObject.getString("Actors"),
                                jsonObject.getString("Plot"),
                                jsonObject.getString("Language"),
                                jsonObject.getString("Country"),
                                jsonObject.getString("Awards"),
                                jsonObject.getString("Metascore"),
                                jsonObject.getString("imdbVotes"),
                                jsonObject.getString("Type"),
                                jsonObject.getString("BoxOffice"),
                                jsonObject.getString("Production"),
                                jsonObject.getString("Website"));


                        tvDescreption.setText(Html.fromHtml(finalResult));
                        rb.setRating(Float.parseFloat(jsonObject.getString("imdbRating")));
                        Picasso.with(MovieDetailsActivity.this).load(jsonObject.getString("Poster"))
                                .fit().into(image);

                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pd.dismiss();
                Toast.makeText(MovieDetailsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(req);
        // pd.show();
    }
}
