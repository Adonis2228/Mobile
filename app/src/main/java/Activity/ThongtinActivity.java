package Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlycuahang.R;
import com.example.quanlycuahang.databinding.ActivityThongtinBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;


public class ThongtinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityThongtinBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityThongtinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbarthongtin);
        Actionbar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            LatLng latLng = new LatLng(21.026856264101458, 105.770804563809);
            float zoom = 30;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}