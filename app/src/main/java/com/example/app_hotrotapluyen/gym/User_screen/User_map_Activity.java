package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.app_hotrotapluyen.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.util.List;
public class User_map_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText editTextLocation;
    private Button buttonSearch;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonSearch = findViewById(R.id.buttonSearch);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(User_map_Activity.this);
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, khởi tạo bản đồ
            initMap();
        }


        // Set OnClickListener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the location from the EditText and perform the search
                String locationName = editTextLocation.getText().toString();
                searchLocation(locationName);
            }
        });

    }
    private void searchLocation(String locationName) {
        LatLng searchedLatLng = getLatLngFromLocationName(locationName);

        // Check if LatLng is valid
        if (searchedLatLng != null) {
            // Clear existing markers and draw the route to the searched location
            mMap.clear();
            drawRoute(searchedLatLng);
        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }
    }
    private LatLng getLatLngFromLocationName(String locationName) {
        return new LatLng(37.7749, -122.4194); // San Francisco, CA as an example
    }
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Enable the "My Location" button for easy navigation
            mMap.setMyLocationEnabled(true);

            // Display the current location with a red marker
            showCurrentLocation();

            // Add marker click listener
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng destinationLatLng = marker.getPosition();
                    drawRoute(destinationLatLng);
                    return true;
                }
            });
        }
    }


    private void showCurrentLocation() {
        // Check if the app has the necessary location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Request location updates
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        // Thêm marker với biểu tượng chấm đỏ
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(currentLatLng)
                                .title("Current Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Marker marker = mMap.addMarker(markerOptions);

                        // Di chuyển camera đến vị trí hiện tại
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); // Zoom level có thể thay đổi

                        // Hiển thị thông báo khi nhấp vào marker (tùy chọn)
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                LatLng destinationLatLng = marker.getPosition();
//                                drawRoute(destinationLatLng);
                                return true;
                            }
                        });
                    } else {
                        Toast.makeText(User_map_Activity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    // Add this method in your User_map_Activity class
    private void drawRoute(LatLng destinationLatLng) {
        // Check if the app has the necessary location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Request location updates
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng originLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        // Create a Directions API request
                        DirectionsApiRequest directions = new DirectionsApiRequest(new GeoApiContext.Builder().apiKey("AIzaSyAI5JAgb0BaxbgIHvO9rxyby300SE6kF6g").build());
                        directions.origin(new com.google.maps.model.LatLng(originLatLng.latitude, originLatLng.longitude))
                                .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude))
                                .mode(TravelMode.DRIVING);  // You can change the travel mode as needed

                        // Execute the request
                        try {
                            DirectionsResult results = directions.await();

                            // Check if the request was successful
                            if (results.routes != null && results.routes.length > 0) {
                                // Extract the polyline points from the route
                                List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());

                                // Draw the polyline on the map
                                if (decodedPath != null && !decodedPath.isEmpty()) {
                                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLUE));
                                } else {
                                    Toast.makeText(User_map_Activity.this, "No route found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(User_map_Activity.this, "No route found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(User_map_Activity.this, "Error getting directions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(User_map_Activity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If the user grants permission, attempt to show current location again
                showCurrentLocation();
            } else {
                // If the user denies permission, handle it accordingly (e.g., show a message)
                Toast.makeText(this, "Location permission denied. Unable to show current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
