package gr.uth.cardshop;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

public class ContactActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private FrameLayout map;
    private MaterialToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        map = findViewById(R.id.fragment_map);
        mToolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng mapLarissa = new LatLng(39.6390, 22.4191);
        this.gMap.addMarker(new MarkerOptions().position(mapLarissa).title("CardShop"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLarissa));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mapLarissa, 15);
        googleMap.moveCamera(cameraUpdate);
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
    }
}