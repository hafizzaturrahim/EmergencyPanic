package hafizzaturrahim.com.emergencypanic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hafizzaturrahim.com.emergencypanic.Tempat;
import hafizzaturrahim.com.emergencypanic.R;

/**
 * Created by Hafizh on 17/05/2016.
 */
public class CustomAdapter extends ArrayAdapter<Tempat> {

    public Context context;
    public ArrayList<Tempat> loc = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Tempat> weatherArrayList) {
        super(context, R.layout.list_view_result, weatherArrayList);
        this.context = context;
        loc = weatherArrayList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Load Custom Layout untuk list
        View rowView = inflater.inflate(R.layout.list_view_result, parent, false);

        //Declarasi komponen
        TextView namaLokasi = (TextView) rowView.findViewById(R.id.namaLokasi);
        TextView alamat = (TextView) rowView.findViewById(R.id.alamat);
        TextView no_telp = (TextView) rowView.findViewById(R.id.no_telp);

        //Set Parameter Value
        namaLokasi.setText(loc.get(position).getNama_tempat());
        alamat.setText(loc.get(position).getAlamat());
        no_telp.setText(loc.get(position).getNo_telp());

        return rowView;
    }
}
