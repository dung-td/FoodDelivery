package com.example.fooddelivery.activity.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.model.DirectionFinder;
import com.example.fooddelivery.model.DirectionFinderListener;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.Route;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DirectionActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    GoogleMap mMaps;

    TextView tv_from, tv_to, tv_time, tv_distance, tv_change;
    ImageButton bt_back;
    ProgressDialog progressDialog;
    ArrayList<Polyline> polylinePaths;
    ArrayList<Marker> originMarkers;
    ArrayList<Marker> destinationMarkers;
    Merchant merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        Init();
    }

    private void Init() {
        tv_from = findViewById(R.id.tv_delivery_from_details);
        tv_to = findViewById(R.id.tv_delivery_to_details);
        tv_time = findViewById(R.id.tv_direc_time);
        tv_distance = findViewById(R.id.tv_direc_distance);
        tv_change = findViewById(R.id.tv_change_address);
        bt_back = findViewById(R.id.direc_bt_back);

        progressDialog = new ProgressDialog(DirectionActivity.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.direc_map);
        mapFragment.getMapAsync(this);

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DirectionActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectionActivity.super.onBackPressed();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Merchant merchant : WelcomeActivity.firebase.merchantList) {
                    merchant.getRoutes().clear();
                }

                WelcomeActivity.firebase.getUser().setAddress(addresses.get(0));

                LatLng toLatLng = new LatLng(WelcomeActivity.firebase.getUser().getAddress().getLatitude(),
                                            WelcomeActivity.firebase.getUser().getAddress().getLongitude());

                sendRequest(toLatLng);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMaps = googleMap;

        getData();
    }

    private void getData() {
        Intent i = getIntent();

        for (Merchant m : WelcomeActivity.firebase.merchantList) {
            if (m.getId().equals(getIntent().getStringExtra("merchantId"))) {
                merchant = m;
                break;
            }
        }

        tv_from.setText(merchant.getAddress().getAddressLine(0));
        tv_to.setText(WelcomeActivity.firebase.getUser().getAddress().getAddressLine(0));

        initRoutes(merchant.getRoutes());
    }

    private void sendRequest(LatLng toLatLng) {
        try {
            LatLng fromLatLng = new LatLng(merchant.getAddress().getLatitude(), merchant.getAddress().getLongitude());
            new DirectionFinder(this, fromLatLng, toLatLng).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.show();

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();

        initRoutes(routes);

        merchant.getRoutes().clear();
        merchant.getRoutes().addAll(routes);
    }

    private void initRoutes(List<Route> route) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route r : route) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(r.startLocation);
            builder.include(r.endLocation);
            LatLngBounds latLngBounds = builder.build();

            mMaps.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMaps.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
                }
            });

            ((TextView) findViewById(R.id.tv_direc_time)).setText(String.format("%s %s", getString(R.string.time_direc), r.duration.text));
            ((TextView) findViewById(R.id.tv_direc_distance)).setText(String.format("%s %s", getString(R.string.distance_direc), r.distance.text));

            originMarkers.add(mMaps.addMarker(new MarkerOptions()
                    .title(merchant.getName())
                    .position(r.startLocation)));
            destinationMarkers.add(mMaps.addMarker(new MarkerOptions()
                    .title(getString(R.string.your_position))
                    .position(r.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < r.points.size(); i++)
                polylineOptions.add(r.points.get(i));

            polylinePaths.add(mMaps.addPolyline(polylineOptions));
        }
    }
}