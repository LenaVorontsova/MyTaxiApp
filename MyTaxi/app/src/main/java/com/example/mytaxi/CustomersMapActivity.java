package com.example.mytaxi;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;
import java.util.ArrayList;
import java.util.List;

public class CustomersMapActivity extends AppCompatActivity implements UserLocationObjectListener, DrivingSession.DrivingRouteListener {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final Point ROUTE_START_LOCATION = new Point(56.852765, 53.206187);
    private final Point ROUTE_END_LOCATION = new Point(56.845072, 53.213836);
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private UserLocationLayer userLocationLayer;
    private TextView priceText;
    private DatabaseReference mDatabase;
    private boolean initial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (initial) {
            return;
        }
        MapKitFactory.setApiKey("68f94b44-d62b-4e49-b77a-95e18f13d4bd");
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        initial = true;
        setContentView(R.layout.activity_customers_map);
        super.onCreate(savedInstanceState);
        initMap();
        findTheNearestDriver();
        priceCalculation();
    }

    private void findTheNearestDriver() {
        mDatabase.child("Drivers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("latitude", "Error getting data", task.getException());
                }
                else {
                    Log.d("latitude", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    private void priceCalculation() {
        Double distance = (Geo.distance(ROUTE_START_LOCATION, ROUTE_END_LOCATION))/1000;
        int price = (int)(distance*50);
        priceText = (TextView) findViewById(R.id.priceTV);
        priceText.setText("Предварительная стоимость поездки: " + price);
    }

    private void initMap() {
        mapView = (MapView) findViewById(R.id.mapview2);

        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(
                new CameraPosition(
                        new Point(56.852765, 53.206187),
                        14,
                        0,
                        0
                )
        );

        mDatabase = FirebaseDatabase.getInstance().getReference();

        requestLocationPermission();

        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(this);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        submitRequest();
    }

    private void submitRequest() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(this, R.drawable.icon),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );

        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onDrivingRoutes(List<DrivingRoute> routes) {
        for (DrivingRoute route : routes) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(Error error) {
        String errorMessage = "Неизвестная ошибка";
        if (error instanceof RemoteError) {
            errorMessage = "Удаленная ошибка";
        } else if (error instanceof NetworkError) {
            errorMessage = "Ошибка сети";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }
}