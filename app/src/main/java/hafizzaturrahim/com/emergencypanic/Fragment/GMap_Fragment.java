package hafizzaturrahim.com.emergencypanic.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hafizzaturrahim.com.emergencypanic.DBHelper;
import hafizzaturrahim.com.emergencypanic.R;
import hafizzaturrahim.com.emergencypanic.Tempat;
import hafizzaturrahim.com.emergencypanic.TinyDB;


@SuppressLint("ValidFragment")
public class GMap_Fragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private boolean hasLoaded = false;
    ArrayList<Tempat> locations = new ArrayList<>();
    DBHelper db;
    int code;

    @SuppressLint("ValidFragment")
    public GMap_Fragment(int code) {
        this.code = code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        db = new DBHelper(getActivity());
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // Perform any camera updates here
        return v;
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (!info.isConnected()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void initializeMap() {
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        TinyDB tinyDB = new TinyDB(getActivity());
        if (checkConnection()) {
            locations = db.getAllTemp();

        } else {
            locations = db.getAllTempat(code);
        }
        if (locations.isEmpty()) {
            Log.v("arraylist", "kosong");
        } else {
            Log.v("arraylist", "isi " + db.getAllTemp());
        }
        double latitude = tinyDB.getDouble("latitude", 0);
        double longitude = tinyDB.getDouble("longitude", 0);
        LatLng myLocation = new LatLng(latitude, longitude);

        // create currentPlaceMarker
//        MarkerOptions currentPlaceMarker = new MarkerOptions().position(
//                new LatLng(latitude, longitude)).title("Lokasi Anda");

        // Changing currentPlaceMarker icon
//        currentPlaceMarker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        //adding marker to result place
        for (int i = 0; i < locations.size(); i++) {
            LatLng thisLocation = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            String jarak = getDistance(myLocation, thisLocation);

            MarkerOptions resultMarker = new MarkerOptions().position(thisLocation)
                    .title(locations.get(i).getNama_tempat()).snippet(jarak+" km");
            resultMarker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            locations.get(i).setJarak(jarak);

            Log.v("Adding marker", "sukses " + locations.get(i).getAlamat());
            googleMap.addMarker(resultMarker);
        }

        // adding currentPlaceMarker
//        googleMap.addMarker(currentPlaceMarker);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                ContextThemeWrapper cw = new ContextThemeWrapper(
                        getActivity(), R.style.MyMaterialTheme);
                // AlertDialog.Builder b = new AlertDialog.Builder(cw);
                LayoutInflater inflater = (LayoutInflater) cw
                        .getSystemService(cw.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.info_marker,
                        null);
                TextView namaTempat = (TextView) v.findViewById(R.id.txtNamaTempat);
                TextView txtJarak = (TextView) v.findViewById(R.id.txtjarak);

                namaTempat.setText(marker.getTitle());
                for (int i = 0; i < locations.size(); i++) {
                    txtJarak.setText(marker.getSnippet());

                }


//                Activity mContext = getActivity();
//                LinearLayout info = new LinearLayout(mContext);
//                info.setOrientation(LinearLayout.VERTICAL);
////
//                TextView title = new TextView(mContext);
//                title.setTextColor(Color.BLUE);
////                title.setGravity(Gravity.CENTER);
//                title.setTypeface(null, Typeface.BOLD);
//                title.setText(marker.getTitle());
////
////                ImageView img = new ImageView(mContext);
////                img.setImageResource(R.drawable.driver);
////
//                info.addView(title);
////                info.addView(img);
////
//                return info;

                return v;
            }
        });
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        double result = distance;
        String dist = distance + " m";

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance + " km";

            result = distance;
        }

        return String.format("%.2f", result);
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible && !hasLoaded) {
                //run your async task here since the user has just focused on your fragment
                initializeMap();
                hasLoaded = true;
            } else {
                Log.v("state", "belum dijalankan " + isFragmentVisible);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
