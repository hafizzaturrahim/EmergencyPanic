package hafizzaturrahim.com.emergencypanic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6;
    private Toolbar toolbar;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);

//        db.deleteTempat();
        //insert kategori if there is no data in table Kategori
        if (db.countKategori() == 0) {
            db.insertKategori(1, "Rumah Sakit");
            db.insertKategori(2, "Pom Bensin");
            db.insertKategori(3, "Kantor Polisi");
            db.insertKategori(4, "PLN");
            db.insertKategori(5, "Pemadam Kebakaran");
        }

        if (db.countTempat() == 0) {
            initializeData();
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("kategori", 1);
                startActivity(intent);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("kategori", 2);
                startActivity(intent);

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("kategori", 3);
                startActivity(intent);

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("kategori", 4);
                startActivity(intent);

            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("kategori", 5);
                startActivity(intent);

            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder about_us = new AlertDialog.Builder(MainActivity.this);
                about_us.setTitle("About Us");
                about_us.setMessage("Created by \n" +
                        "MUH HAFIZH I\n" +
                        "RIZKI NANDA MUSTAQIM\n" +
                        "ILHAM KHOIRUL ILMI\n" +
                        "BAGUS PRIYAMBADA\n" +
                        "RATRI MAYANGSARI\n " +
                        "\nPAPB-B 2016 \n" +
                        "SI FILKOM UB");
                about_us.show();
            }
        });
    }

    private void initializeData() {
        db.insertTempat(4, "Rayon Malang Kota", "Jalan Basuki Rahmad 100 Malang", "(0341) 355244", -7.974574, 112.630023, null);
        db.insertTempat(4, "Rayon Blimbing", "Jl. Raya Mangliawan No. 3 Blimbing Malang", "(0341) 791114", -7.950793, 112.667961, null);
        db.insertTempat(4, "Rayon Ngantang", "Jl. Raya Ngantang No. 4 Ngantang", "(0341) 521220", -7.855952, 112.369598, null);
        db.insertTempat(4, "Rayon Kebunagung", "Jl. Satsui Tubun Kebonagung Malang", "(0341) 835943", -8.021652, 112.625289, null);
        db.insertTempat(4, "Rayon Dinoyo", "Jl. MT. Haryono No. 189 Dinoyo Malang", "(0341) 581223", -7.947824, 112.613926, null);
        db.insertTempat(4, "Rayon Singosari", "Jl. Kertanegara No. 64 Singosari", "(0341) 326034", -7.889097, 112.666039, null);
        db.insertTempat(4, "Rayon Lawang", "Jl. Pungkur Argo No. 12 Lawang", "(0341) 426027", -7.835658, 112.695173, null);
        db.insertTempat(4, "Rayon Batu", "Jl. Trunojoyo No. 14-A Batu", "(0341) 593045", -7.865474, 112.509454, null);
        db.insertTempat(4, "Rayon Kepanjen", "Jl. Panji No. 2 Kepanjen", "(0341) 395033", -8.134222, 112.57374, null);
        db.insertTempat(4, "Rayon Tumpang", "Jl. Tulusayu Tumpang", "(0341) 787277", -8.019264, 112.763716, null);
        db.insertTempat(4, "Rayon Bululawang", "Jl. Raya Wandanpuro Bululawang", "(0341) 833018", -8.069097, 112.640366, null);
        db.insertTempat(4, "Rayon Gondanglegi", "Jl. Pangeran Diponegoro No. 16 Gondanglegi", " (0341) 878213", -8.176499, 112.63631, null);
        db.insertTempat(4, "Rayon Sumberpucung", "Jl. Raya Basuki Rahmad No. 9 Karangkates Sumberpucung", "(0341) 383389", -8.158967, 112.459883, null);
        db.insertTempat(4, "Surabaya Utara", "Jl. Gemblongan 64 Surabaya", "(031) 53401514", -7.25416, 112.73685, null);
        db.insertTempat(4, "Surabaya Selatan", "Jl. Ngagel Timur 14-16 Surabaya", "(031) 504257273", -7.288795, 112.749777, null);
        db.insertTempat(4, "Surabaya Barat", "Jl Raya Taman No. 48D Sepanjang Surabaya", "(031) 7668488", -7.354221, 112.695025, null);
        db.insertTempat(4, "Gresik", "Jl. Dr. Wahidin Sudiro Husodo No.134 Gresik", "(031) 39742913", -7.162606, 112.632371, null);
        db.insertTempat(4, "Sidoarjo", "Jl. A. Yani No.47-49 Sidoarjo", "(031) 89554104", -7.450552, 112.718827, null);
        db.insertTempat(4, "Pasuruan", "Jl. Panglima Sudirman No. 69 Pasuruan", "(0343) 4265167", -7.652556, 112.900157, null);
        db.insertTempat(4, "Mojokerto", "Jl. Ra. Basuni 67 Mojokerto", "(0321) 322705", -7.494236, 112.42479, null);
        db.insertTempat(4, "Jember", "Jl. Gajah Mada No.198 Jember", "(0331) 484641-2", -8.179052, 113.677568, null);

        db.insertTempat(5, "Daerah Istimewa Aceh", "JL. Mayjen Sutoyo, Kuala Simpang, Aceh Tamiang,Bukit Tempurung,Banda Aceh", "(+62 641) 31113", 4.285547, 98.060584, null);
        db.insertTempat(5, "Kota Medan", " Jl. Candi Borobudur No. 2 Medan", "(061) 4515356", 3.591405, 98.670941, null);
        db.insertTempat(5, "Sumatra Barat", "Dinas Damkar Kota Padang Jl Rasuna Said No 56", " (0751) 28558", -0.930284, 100.362687, null);
        db.insertTempat(5, "Sumatra Selatan", "Jl. K.H.A. Azhari,13 Ulu,Seberang Ulu II,Kota Palembang, Sumatera Selatan, Indonesia", "(0735) 322113", 0.91144, 104.774206, null);
        db.insertTempat(5, "Kepulauan Riau", "Kantor Damkar Kota Tanjungpinang Jl. Ir Sutami no.1 Tanjungpinang Kepulauan Riau", "(0771) 20949", 3.591405, 104.455718, null);
        db.insertTempat(5, "Bali", "Jl. Iman Bonjol No.176, Denpasar, Bali 80119", "(0361) 484013", -8.674261, 115.206393, null);
        db.insertTempat(5, "Bali", "Jl. Candi Borobudur No. 2 Medan", "(061) 411333", -8.617206, 115.182441, null);
        db.insertTempat(5, "Pemadam Kebakaran Kabupaten Pandeglang", "Jl. Mayor Widagdo No. 4 Pandeglang", "(0253) 201113", -6.304069, 106.223034, null);
        db.insertTempat(5, "Dinas Pemadam Kebakaran Kota Bengkulu", "Jl. Bhayangkara No.47 Kota Bengkulu", "(0253) 201284", -3.835859, 102.312309, null);
        db.insertTempat(5, "DinDamkar Kota Gorontalo", "JL Jamaludin Malik NO. 52, Gorontalo, Propinsi Gorontalo", "(0435) 822602", 0.549851, 123.058361, null);
        db.insertTempat(5, "DinDamkar Kota Jakarta", "Jl. K.H.ZainulArifin No. 71", "(021) 6330325", -6.161527, 106.809355, null);
        db.insertTempat(5, "DinDamkar Kota Jambi", "Jl.Hos cokroaminoto NO 113", "(0741) 7033082", -1.619313, 103.595769, null);
        db.insertTempat(5, "Dindamkar Kota Bandung", "Jl. Sukabumi 17", "(022)7207113", -6.916717, 107.634966, null);
        db.insertTempat(5, "DinDamkar Jawa Tengah", "JL. MAYOR KUSMANTO No. 109 KLATEN", "(0272) 324 113", -7.689853, 110.604265, null);
        db.insertTempat(5, "Damkar Kab. Bantul", "Jl. Wachid hasyim Sumuran Bantul Yogyakarta", "(0274) 367401", -7.776395, 110.419571, null);
        db.insertTempat(5, "DinDamkar Jawa Timur", "Jl. Kartini,Ngaglik,Kec. Batu,Kota Batu, Jawa Timur", "(0355) 802791", -7.912378, 112.741114, null);

    }
}
