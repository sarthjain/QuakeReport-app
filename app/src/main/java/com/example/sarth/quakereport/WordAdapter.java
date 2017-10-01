package com.example.sarth.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sarth on 8/23/2017.
 */

public class WordAdapter extends ArrayAdapter<word> {

    public WordAdapter(Context context,List<word> list)
    {
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        word currentword = getItem(position);

        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentword.getmag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        DecimalFormat format = new DecimalFormat("0.0");
        String mag=format.format(currentword.getmag());
        magTextView.setText(mag);

        String place=currentword.getplace();
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.place);
        TextView nearTextView = (TextView) listItemView.findViewById(R.id.near);
        if(!place.contains("of")) {
            nearTextView.setText("Near the");
            nameTextView.setText(place);
        }
        else
        {
            String[] ar=place.split("of ");
            nearTextView.setText(ar[0]+"of");
            nameTextView.setText(ar[1]);
        }
        Date dateObject = new Date(currentword.gettime());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        Log.v(place,dateToDisplay);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(dateToDisplay);

        dateFormatter = new SimpleDateFormat("h:mm a");
        dateToDisplay = dateFormatter.format(dateObject);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(dateToDisplay);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
