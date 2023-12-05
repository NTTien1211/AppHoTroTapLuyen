package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
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
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class User_Profile_Fragment extends Fragment implements OnMapReadyCallback {
    TextView User_Male_pt_inormation, User_phone_user_inormation, User_email_user_inormation,weight_ptIn_User,Height_ptIn_dgree
            ,BMI_user_profile ,User_user_inormation;
    Button btn_update_user_profile;
    UserModel userModel;
    ImageView profile_pic_image_view_profile;
    String idUser;
    LinearLayout color_back_bmi;
    TextView locationInfoTextView;
    private GoogleMap mMap;
    private Geocoder geocoder;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__profile_, container, false);
        User_Male_pt_inormation = view.findViewById(R.id.User_Male_pt_inormation);
        User_phone_user_inormation = view.findViewById(R.id.User_phone_user_inormation);
        User_email_user_inormation = view.findViewById(R.id.User_email_user_inormation);
        User_user_inormation = view.findViewById(R.id.User_user_inormation);
        weight_ptIn_User = view.findViewById(R.id.weight_ptIn_User);
        Height_ptIn_dgree = view.findViewById(R.id.Height_ptIn_dgree);
        profile_pic_image_view_profile = view.findViewById(R.id.profile_pic_image_view_profile);
        BMI_user_profile = view.findViewById(R.id.BMI_user_profile);
        btn_update_user_profile = view.findViewById(R.id.btn_update_user_profile);
        color_back_bmi = view.findViewById(R.id.color_back_bmi);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idUser);

        btn_update_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getActivity() , User_Repair_Inf_Activity.class);
                startActivity(intent);
            }
        });
        geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        locationInfoTextView = view.findViewById(R.id.locationInfoTextView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Check for location permissions
        if (checkLocationPermission()) {
            // If permissions are granted, initialize the map
            initMap(view);
        } else {
            // Request location permissions
            requestLocationPermission();
        }

        return view;
    }
    private class SelecDatabase extends AsyncTask<String, Void, UserModel> {

        @Override
        protected UserModel  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(idUser));

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String Userid = resultSet.getString("ID_User");
                        String email = resultSet.getString("Email");
                        String weight = resultSet.getString("Weight");
                        String phone = resultSet.getString("Phone");
                        String hight = resultSet.getString("Height");
                        String gender = resultSet.getString("Gender");
                        String BMI = resultSet.getString("BMI");
                        String img = resultSet.getString("IMG");
                        userModel = new UserModel(Userid , name,phone,email,weight, hight ,gender,BMI, img);
                        return userModel;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return  userModel ;
        }

        @Override
        protected void onPostExecute(UserModel userList) {
            super.onPostExecute(userList);
            if (!isAdded()) {
                // Fragment đã bị detach, không thể truy cập context nữa
                return;
            }

            if (userList != null ) {
                User_Male_pt_inormation.setText(userList.getGender());
                User_user_inormation.setText(userList.getName());
                User_phone_user_inormation.setText(userList.getPhone());
                User_email_user_inormation.setText(userList.getEmail());
                weight_ptIn_User.setText(userList.getWeight());
                Height_ptIn_dgree.setText(userList.getHight());
                String img = userList.getImg();
                if (img == null || img.isEmpty()){
                    profile_pic_image_view_profile.setImageDrawable(getResources().getDrawable(R.drawable.person_icon));
                }
                else {
                    Picasso.get().load(img).transform(new CircleTransform()).into(profile_pic_image_view_profile);
                }
                if (userList.getWeight() == null && userList.getHight() ==null ){
                    BMI_user_profile.setText("0");
                }
                else {

                    BMI_user_profile.setText(userList.getBMI());
                    String text =  userList.getBMI();
                    float bmi = Float.parseFloat(text.replace("," ,"."));
                    if (bmi <= 18.5) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.lavender)); // Thay thế colorBlue bằng màu bạn muốn sử dụng
                    } else if (bmi <= 24.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_overweight)); // Thay thế colorGreen bằng màu bạn muốn sử dụng
                    } else if (bmi <= 29.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_obesity)); // Thay thế colorOrange bằng màu bạn muốn sử dụng
                    } else if (bmi <= 34.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_underweight)); // Thay thế colorRed bằng màu bạn muốn sử dụng
                    } else {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.purple_dam)); // Thay thế colorPurple bằng màu bạn muốn sử dụng
                    }

                }

            } else {
                Toast.makeText(getContext(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        // Request location permissions
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void initMap(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions again
        if (checkLocationPermission()) {
            // Show the "My Location" button for easy navigation to the current location
            mMap.setMyLocationEnabled(true);

            // Show the current location with a red dot icon
            showCurrentLocation();
        }
    }

    // Inside UserMapFragment class

    private void showCurrentLocation() {
        // Get the last known location
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            try {
                                // Reverse geocode the location to get an address
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    Address address = addresses.get(0);

                                    // Update the TextView with the address information
                                    updateLocationInfoTextView(address);

                                    // Add a marker with a red dot icon
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(currentLatLng)
                                            .title(address.getAddressLine(0)) // Display the first line of the address
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    Marker marker = mMap.addMarker(markerOptions);

                                    // Move the camera to the current location
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); // Zoom level can be changed

                                    // Display a message when clicking the marker (optional)
                                    mMap.setOnMarkerClickListener(marker1 -> {
                                        Toast.makeText(requireActivity(), "Marker Clicked", Toast.LENGTH_SHORT).show();
                                        return true;
                                    });
                                } else {
                                    Toast.makeText(requireActivity(), "Unable to get address for current location", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateLocationInfoTextView(Address address) {
        if (locationInfoTextView != null) {
            StringBuilder locationInfo = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                locationInfo.append(address.getAddressLine(i)).append("\n");
            }
            locationInfoTextView.setText(locationInfo.toString().trim());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If the user grants permission, initialize the map
                initMap(getView());
            } else {
                Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}