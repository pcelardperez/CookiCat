package com.example.pedro.cookicat.dummy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pedro.cookicat.R;

import java.util.List;

/**
 * Created by pedro on 16/02/15.
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    List<Contenido> rowItem;

    public CustomAdapter(Context context, List<Contenido> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {

        return rowItem.size();
    }


    @Override
    public Object getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }


    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }


        TextView txtIngrediente = (TextView) convertView.findViewById(R.id.txtIngrediente);
        TextView txtCantidad = (TextView) convertView.findViewById(R.id.txtCantidad);
        TextView txtUnidad = (TextView) convertView.findViewById(R.id.txtUnidad);

        Contenido row_pos = rowItem.get(position);
        // setting the image resource and title
        txtIngrediente.setText(row_pos.getItemIngrediente());
        txtCantidad.setText(row_pos.getItemCantidad());
        txtUnidad.setText(row_pos.getItemUnidad());

        return convertView;
    }
}
