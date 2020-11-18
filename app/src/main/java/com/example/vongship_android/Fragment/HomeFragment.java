package com.example.vongship_android.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.vongship_android.Activity.FoodDeliveryActivity;
import com.example.vongship_android.Adapter.CategoriesAdapter;
import com.example.vongship_android.Adapter.BannerAdapter;
import com.example.vongship_android.Activity.MapsActivity;

import com.example.vongship_android.Class.DownloadImageTask;
import com.example.vongship_android.DTO.Categories;
import com.example.vongship_android.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.vongship_android.R.id.location_click;

public class HomeFragment extends Fragment {
    LinearLayout location;
    CardView gotoFoodDelivery;
    ArrayList<Categories> categoriesArrayList;
    CategoriesAdapter categoriesAdapter;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        location = root.findViewById(location_click);
        gotoFoodDelivery = root.findViewById(R.id.gotoFoodDelivery);
        ViewPager viewPager = root.findViewById(R.id.viewPager);
        BannerAdapter adapter = new BannerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        envent();
        int permission_fine_loc = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_coarse_loc = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission_fine_loc != PackageManager.PERMISSION_GRANTED
                || permission_coarse_loc != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
        TextView txtAdress =root.findViewById(R.id.txt_Address);
        TextView txtDate =root.findViewById(R.id.txt_date);
        if(VT() != null){
            txtAdress.setText(VT());
        }else {
            txtAdress.setText("");
        }


        new DownloadImageTask((ImageView) root.findViewById(R.id.IMGprofile_home))
                .execute("https://firebasestorage.googleapis.com/v0/b/doanltdd-60a15.appspot.com/o/Image%2FprofileImage.jpg?alt=media&token=40d48a63-1ac3-4e2c-946d-4b8515f79c62");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d MMMM, ''yyyy");
        String currentDateandTime = sdf.format(new Date());
        txtDate.setText(currentDateandTime);

        RecyclerView recyclerView =root.findViewById(R.id.CuaHangRecyclerView);
        RecyclerView recyclerView1 =root.findViewById(R.id.CuaHangKMRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        CuaHangMoi();
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView1.setAdapter(categoriesAdapter);

        return root;
    }
    private void makeRequest() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        Intent refresh = new Intent(getContext(), HomeFragment.class);
        startActivity(refresh);
        getActivity().finish();
    }

    public void CuaHangMoi(){
        categoriesArrayList = new ArrayList<>();
        categoriesArrayList.add(new Categories(1,"Bún Đậu Cầu Gỗ - Phan Xích Long",R.drawable.quan2));
        categoriesArrayList.add(new Categories(2,"Trà Tiên Hưởng - Khánh Hội",R.drawable.quan3));
        categoriesArrayList.add(new Categories(3,"Heekcaa Original",R.drawable.quan4));
        categoriesArrayList.add(new Categories(4,"TocoToco Bubble Tea",R.drawable.quan5));
        categoriesArrayList.add(new Categories(5,"Cha-Jiang Việt Nam - Calmette",R.drawable.quan6));
        categoriesArrayList.add(new Categories(7,"Trà Sữa, Trà Trái Cây Thỏ Ngọc",R.drawable.quan7));
        categoriesAdapter = new CategoriesAdapter(categoriesArrayList,getActivity());
    }
    public void CuaHangKM(){

    }


    public String VT() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        Criteria criteria = new Criteria();

        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        double latitude = 0;
        double longitude = 0;
        if(lastLocation!= null){
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }
        Geocoder geoCoder = new Geocoder(getActivity(),Locale.getDefault());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
        String address=null;
        if(bestMatch !=null){
            address= bestMatch.getAddressLine(0);
        }

        return address;

    }


    public void envent(){
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        gotoFoodDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodDeliveryActivity.class);
                startActivity(intent);
            }
        });
    }


    LocationListener locationListener= new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude= location.getLongitude();
            double lat= location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };

}