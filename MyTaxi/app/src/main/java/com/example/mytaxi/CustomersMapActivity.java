package com.example.mytaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class CustomersMapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("68f94b44-d62b-4e49-b77a-95e18f13d4bd");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_customers_map);
        mapView = (MapView)findViewById(R.id.mapview2);
        mapView.getMap().move(
                new CameraPosition(new Point(55.751575, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}