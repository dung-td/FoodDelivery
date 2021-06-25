package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Merchant;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

public class InfoFragment extends Fragment {
    GoogleMap mMaps;
    TextView tv_address;
    Merchant merchant;

    public InfoFragment(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        tv_address = v.findViewById(R.id.tv_address);
        tv_address.setText(merchant.getAddress().getAddressLine(0));


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMaps = googleMap;

                mMaps.addMarker(new MarkerOptions()
                        .title(merchant.getName())
                        .position(new LatLng(
                                merchant.getAddress().getLatitude(),
                                merchant.getAddress().getLongitude()
                        )));

                mMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        merchant.getAddress().getLatitude(),
                        merchant.getAddress().getLongitude()
                ), 15), 5000, null);

            }
        });

        return v;
    }
}
