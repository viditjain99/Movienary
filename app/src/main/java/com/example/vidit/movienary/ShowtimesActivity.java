package com.example.vidit.movienary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowtimesActivity extends AppCompatActivity
{
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView showtimesRecyclerView;
    FusedLocationProviderClient client;
    LocationCallback callback;
    ShowtimeResponse showtimeResponse;
    ShowtimesAdapter adapter;
    ArrayList<Showtime> showtimes=new ArrayList<>();
    String lat="";
    String longi="";

    @Override
    public void onStart()
    {
        super.onStart();
        LocationRequest request=new LocationRequest();
        request.setInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setFastestInterval(3000);
        lat="";
        longi="";
        callback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if(locationResult!=null)
                {
                    List<Location> locations=locationResult.getLocations();
                    Location location=locations.get(0);
                    lat=String.valueOf(location.getLatitude());
                    longi=String.valueOf(location.getLongitude());

                }
                super.onLocationResult(locationResult);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(request,callback, null);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        client.removeLocationUpdates(callback);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);
        showtimesRecyclerView=findViewById(R.id.showtimesRecyclerView);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("Showtimes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Intent intent=getIntent();
        final String id=intent.getStringExtra("movieId");
        adapter=new ShowtimesAdapter(ShowtimesActivity.this,showtimes);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ShowtimesActivity.this,LinearLayoutManager.VERTICAL,false);
        showtimesRecyclerView.setLayoutManager(layoutManager);
        showtimesRecyclerView.setAdapter(adapter);
        client= LocationServices.getFusedLocationProviderClient(ShowtimesActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    String l=location.getLatitude()+","+location.getLongitude();
                    Call<ShowtimeResponse> call=ApiClient.getMoviesService().getShowtimes(id,l);
                    call.enqueue(new Callback<ShowtimeResponse>() {
                        @Override
                        public void onResponse(Call<ShowtimeResponse> call, Response<ShowtimeResponse> response)
                        {
                            showtimeResponse=response.body();
                            if(showtimeResponse==null)
                            {
                                return;
                            }
                            ArrayList<Showtime> results=showtimeResponse.showtimes;
                            showtimes.addAll(results);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<ShowtimeResponse> call, Throwable t) {

                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
