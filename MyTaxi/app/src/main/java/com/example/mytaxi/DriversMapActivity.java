package com.example.mytaxi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.List;

public class DriversMapActivity extends AppCompatActivity  {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("a1b5d0b8-480b-419a-b732-d1f09def9c28");
        MapKitFactory.initialize(DriversMapActivity.this);
        
        setContentView(R.layout.activity_drivers_map);
        mapView = (MapView)findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(DriversMapActivity.this, "Доступ разрешен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(DriversMapActivity.this, "Доступ запрещен\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Чтобы использовать приложение, разрешите доступ к метоположению")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
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