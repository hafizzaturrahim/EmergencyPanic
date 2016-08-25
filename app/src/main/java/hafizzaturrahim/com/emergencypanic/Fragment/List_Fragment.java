package hafizzaturrahim.com.emergencypanic.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hafizzaturrahim.com.emergencypanic.Adapter.CustomAdapter;
import hafizzaturrahim.com.emergencypanic.DBHelper;
import hafizzaturrahim.com.emergencypanic.R;
import hafizzaturrahim.com.emergencypanic.ServiceHandler;
import hafizzaturrahim.com.emergencypanic.Tempat;
import hafizzaturrahim.com.emergencypanic.TinyDB;

public class List_Fragment extends Fragment implements LocationListener {
    ArrayList<Tempat> locations = new ArrayList<>();
    ListView listLocation;
    double lat, lng;
    int counter = 0;
    LocationManager lm;
    TinyDB tinyDB;
    DBHelper db;
    int code;
    FloatingActionButton fab;

    public List_Fragment() {
    }

    @SuppressLint("ValidFragment")
    public List_Fragment(int code) {
        this.code = code;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listLocation = (ListView) rootView.findViewById(R.id.lv_location);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        tinyDB = new TinyDB(getActivity());
        db = new DBHelper(getActivity());

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (checkConnection()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return rootView;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.v("arraylist not connected", "ga masuk");
        } else {
            Toast.makeText(getActivity(), "No internet connection. Offline database activated", Toast.LENGTH_SHORT).show();
        }

        if (code > 3 || !checkConnection()) {
            fab.setVisibility(View.GONE);
        }
        locations.clear();
        new GetDataWebService(getActivity(), checkConnection()).execute();
        return rootView;
    }

    public boolean checkConnection() {
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

    public ArrayList<Tempat> getLocations() {
        return locations;
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        counter = 1;
//        Log.v("lat, long : ", String.valueOf(lat));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class GetDataWebService extends AsyncTask<String, Void, Void> {
        private ProgressDialog pDialog;
        Context mContext = getActivity();
        AlertDialog.Builder builder;
        boolean statusConnection;
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        public GetDataWebService(Context mContext, boolean statusConnection) {
            this.mContext = mContext;
            this.statusConnection = statusConnection;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            builder = new AlertDialog.Builder(mContext);

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Fetching data");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            // URL to get contacts JSON
            boolean isLocal = false;
            String type = null;
            switch (code) {
                case 1:
                    type = "hospital";
                    isLocal = false;
                    break;
                case 2:
                    type = "gas_station";
                    isLocal = false;
                    break;
                case 3:
                    type = "police";
                    isLocal = false;
                    break;
                case 4:
                    isLocal = true;
                    break;
                case 5:
                    isLocal = true;
                    break;
            }

            if (statusConnection && !isLocal) {
                while (counter == 0) {
                    //looping to wait latitude and longitude
                }
                String jsonUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + lat + "," + lng + "&radius=5000&" +
                        "types=" + type + "&sortBy=distance&" +
                        "key=AIzaSyAobxtY-vxjoW9rhEaaWxVOiLN9wxq7sJQ";

                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(jsonUrl, ServiceHandler.GET);

                Log.v("url json : ", jsonUrl);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray resultArray = jsonObj.getJSONArray("results");
                        final int numberOfItemsInResp = resultArray.length();

                        db.deleteTemp();
//                     looping through All Places
                        for (int i = 0; i < numberOfItemsInResp; i++) {
                            Tempat lokasi = new Tempat();
                            JSONObject r = resultArray.getJSONObject(i);

                            JSONObject geometry = r.getJSONObject("geometry");
                            JSONObject loc = geometry.getJSONObject("location");
                            String place_id = r.getString("place_id");

                            //get telp. number from another web service
                            String jsonUrlDetail = "https://maps.googleapis.com/maps/api/place/details/json?" +
                                    "placeid=" + place_id + "&key=AIzaSyAobxtY-vxjoW9rhEaaWxVOiLN9wxq7sJQ";
                            String jsonStr2 = sh.makeServiceCall(jsonUrlDetail, ServiceHandler.GET);

                            JSONObject jObj = new JSONObject(jsonStr2);
                            JSONObject result = jObj.getJSONObject("result");

                            String nama_tempat = r.getString("name");

                            boolean isRightData = false;

                            switch (code) {
                                case 1:
                                    if (nama_tempat.toLowerCase().contains("rumah sakit") ||
                                            nama_tempat.toLowerCase().contains("rs") ||
                                            nama_tempat.toLowerCase().contains("puskesmas")) {
                                        isRightData = true;
                                    }
                                    break;
                                case 2:
                                    if (nama_tempat.toLowerCase().contains("spbu")) {
                                        isRightData = true;
                                    }
                                    break;
                                case 3:
                                    if (nama_tempat.toLowerCase().contains("polisi") ||
                                            nama_tempat.toLowerCase().contains("pols") ||
                                            nama_tempat.toLowerCase().contains("satlantas")) {
                                        isRightData = true;
                                    }
                                    break;
                            }

                            if (isRightData) {
                                lokasi.setPlace_id(place_id);
                                lokasi.setId_kategori(code);
                                lokasi.setNama_tempat(nama_tempat);
                                lokasi.setAlamat(r.getString("vicinity"));

                                if (jsonStr2 != null) {
                                    try {
                                        lokasi.setNo_telp(result.getString("formatted_phone_number"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.v("ServiceHandler", "Tidak bisa mendapat nomor telepon");
                                    }
                                }
                                lokasi.setLatitude(Double.parseDouble(loc.getString("lat")));
                                lokasi.setLongitude(Double.parseDouble(loc.getString("lng")));

                                locations.add(lokasi);

                                db.insertTempat_TEMP(lokasi);
                                Log.v("Hasil temp", String.valueOf(db.countTemp()));
                            }
//                        locations.add(location);

                        }
                        tinyDB.putListObject("Tempat", locations);
                        tinyDB.putDouble("latitude", lat);
                        tinyDB.putDouble("longitude", lng);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("ServiceHandler", "Tidak bisa mendapat data dari URL yang diminta");
                    }
                } else {
                    Log.v("ServiceHandler", "Tidak bisa mendapat data dari URL yang diminta");
                    pDialog.dismiss();
                }
            } else {
                locations = db.getAllTempat(code);
                if (locations.isEmpty()) {
                    Log.v("arraylist not connected", "kosong");
                } else {
                    Log.v("arraylist not connected", "isi " );
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            CustomAdapter adapter = new CustomAdapter(mContext, locations);
            listLocation.setAdapter(adapter);
            listLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String phoneNum = locations.get(position).getNo_telp();

                    builder.setTitle("Lakukan Panggilan");
//                    builder.setIcon(R.drawable.common_google_signin_btn_text_light);
                    if (phoneNum != null) {
                        builder.setMessage("Apakah anda ingin menelpon " + locations.get(position).getNo_telp() + " ?");
                    } else {
                        builder.setMessage("Nomor telepon tidak ada...");

                    }
                    builder.setPositiveButton("Ya",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Calling(phoneNum);
                                    dialog.dismiss();
                                }
                            });
                    builder.setNegativeButton("Tidak",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Simpan hasil untuk offline?", Snackbar.LENGTH_LONG)
                            .setAction("SAVE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbar1 = Snackbar.make(view, "Data telah disimpan!", Snackbar.LENGTH_SHORT);
                                    for (int i = 0; i < locations.size(); i++) {
                                        Tempat place = locations.get(i);
                                        Log.v("Place id utk disave ", place.getPlace_id());
                                        db.insertTempat(place.getId_kategori(),
                                                place.getNama_tempat(),
                                                place.getAlamat(),
                                                place.getNo_telp(),
                                                place.getLatitude(),
                                                place.getLongitude(),
                                                place.getPlace_id());
                                    }
                                    snackbar1.show();
                                }
                            });
                    snackbar.show();
                }
            });

        }

        protected void Calling(String phoneNum) {
            Log.i("Make call", "");
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);

            if (phoneNum != null) {
                phoneIntent.setData(Uri.parse("tel:" + phoneNum));
            }

            try {
                startActivity(phoneIntent);
                Log.i("Finished making a call", "");
            } catch (android.content.ActivityNotFoundException ex) {

            }
        }

    }


}
