package com.example.fireintegration;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MoveTrack extends FragmentActivity implements OnMapReadyCallback {
Location location1;
  LatLng point;
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST = 100;
    int n = 0;
    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_track);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
        }
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {

//If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//...then start the GPS tracking service//

            startTrackerService();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, MoveTrack.class));

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

//Close MainActivity//
        buildNotification();
        loginToFirebase();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

//    private void buildNotification() {
//        String stop = "stop";
//        registerReceiver(stopReceiver, new IntentFilter(stop));
//        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
//                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
//
//// Create the persistent notification//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(getString(R.string.tracking_enabled_notif))
//
////Make this notification ongoing so it can’t be dismissed by the user//
//
//                .setOngoing(true)
//                .setContentIntent(broadcastIntent)
//                .setSmallIcon(R.drawable.tracking_enabled);
//        startForeground(1, builder.build());
//    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

// Create the persistent notification//
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))

//Make this notification ongoing so it can’t be dismissed by the user//

                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.com_facebook_tooltip_black_topnub);
//        startForeground(1, builder.build());

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//Unregister the BroadcastReceiver when the notification is tapped//

            unregisterReceiver(stopReceiver);

//Stop the Service//

            // stopSelf();
        }
    };

    private void loginToFirebase() {

//Authenticate with Firebase, using the email and password we created earlier//

        String email = getString(R.string.test_email);
        String password = getString(R.string.test_password);

//Call OnCompleteListener if the user is signed in successfully//

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

//If the user has been authenticated...//

                if (task.isSuccessful()) {

//...then call requestLocationUpdates//

                    requestLocationUpdates();
                } else {

//If sign in fails, then log the error//

                    Log.d("Fiebase", "Firebase authentication failed");
                }
            }
        });

    }


;

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

        request.setInterval(10000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();

                    if (location != null ) {
                     //   list.add(new LatLng(location.getLatitude(),location.getLongitude()));
                       if(n==0)
                       {           point = new LatLng(location.getLatitude(),location.getLongitude());
                           options.add(point);
                            location1= location;
                           LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                           mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                           mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at your location"));
                       }
                        if( n!=0 && location1!=null && location1!=location) {
//Save the location data to the database//

                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at your location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            ref.setValue(location);
//                            Polyline line = mMap.addPolyline(new PolylineOptions()
//                                    .add(new LatLng(location1.getLatitude()*1e6, location1.getLongitude()*1e6), new LatLng(location.getLatitude()*1e6, location.getLongitude()*1e6))
//                                    .width(15)
//                                    .color(Color.RED));
//                            line.getWidth();
//                            Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
//                                    .clickable(true)
//                                    .add(
//                                            new LatLng(location1.getLatitude(), location1.getLongitude()),
//
//                                            new LatLng(location.getLatitude(), location.getLongitude())));
//                            polyline1.setVisible(true);


                                 point = new LatLng(location.getLatitude(),location.getLongitude());
                                options.add(point);

                            Polyline line = mMap.addPolyline(options);

                            // Position the map's camera near Alice Springs in the center of Australia,
                            // and set the zoom factor so most of Australia shows on the screen.
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 4));


                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at your location"));
                                location1=location;
                        } }
                }
            }, null);
        }
    }
}