package com.example.kumar.raghawendra.mapproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by raghawendra.kumar on 12-01-2016.
 */
public class MainActivity  extends AppCompatActivity implements View.OnClickListener {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText source, destination;
    Button submit;
    Place fromPlace, toPlace;
    LatLng sourceLatlong, destLatlong;
    private static int flag =0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        source = (EditText)findViewById(R.id.source);
        source.setOnClickListener(this);
        destination = (EditText)findViewById(R.id.destination);
        destination.setOnClickListener(this);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.submit){
            System.out.println("Submit clicked!!!");
            flag = 0;
            finish();
            Intent intent = new Intent(this, MapsActivity.class);
            Bundle bd = new Bundle();
            bd.putParcelable("source", sourceLatlong);
            bd.putParcelable("destination", destLatlong);
            intent.putExtra("bundle",bd);
            startActivity(intent);
        }
        else if(v.getId()== R.id.source)
            flag = 1;
        else
            flag = 2;

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Place place = PlaceAutocomplete.getPlace(this, data);
                if(flag ==1){
                    fromPlace = PlaceAutocomplete.getPlace(this, data);
                    sourceLatlong = fromPlace.getLatLng();
                    source.setText(fromPlace.getName());
                    System.out.println("Place: " + fromPlace.getName());
                }else{
                    toPlace = PlaceAutocomplete.getPlace(this, data);
                    destLatlong = toPlace.getLatLng();
                    destination.setText(toPlace.getName());
                    System.out.println("Place: " + toPlace.getName());
                }flag =0;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                System.out.println(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
