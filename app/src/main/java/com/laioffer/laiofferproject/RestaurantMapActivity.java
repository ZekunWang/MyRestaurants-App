package com.laioffer.laiofferproject;

import android.support.v4.app.FragmentActivity;;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class RestaurantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    public final static String EXTRA_LATLNG = "EXTRA_LATLNG";
    private LatLng[] toMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.restaurant_map);

        // This function automatically initializes the maps system and the view.
        mapFragment.getMapAsync(this);

        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            toMark = new LatLng[bundle.size()];
            for (int i = 0; i < bundle.size(); i++) {
                toMark[i] = bundle.getParcelable(EXTRA_LATLNG + i);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (toMark != null) {
            if (toMark.length == 1) {
                int pos = getIntent().getIntExtra("position", 0);
                map.addMarker(new MarkerOptions().position(toMark[0]).title(RestaurantListActivity.rests.get(pos).getName()));
            } else {
                for (int i = 0; i < toMark.length; i++) {
                    map.addMarker(new MarkerOptions().position(toMark[i]).title(RestaurantListActivity.rests.get(i).getName()));
                }
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(toMark[toMark.length / 2]));
            map.animateCamera(CameraUpdateFactory.zoomTo((toMark.length == 1 ? 15 : 13)), 2000, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
