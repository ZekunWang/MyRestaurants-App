package com.laioffer.laiofferproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantGridFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView gridView;
    private DataService dataService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_grid, container, false);
        gridView = (GridView) view.findViewById(R.id.restaurant_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Restaurant r = (Restaurant) gridView.getItemAtPosition(position);
                //Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                // Prepare all the data we need to start map activity.
                Bundle bundle = new Bundle();
                bundle.putParcelable(
                        RestaurantMapActivity.EXTRA_LATLNG + '0',
                        new LatLng(r.getLat(), r.getLng()));
                Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                intent.putExtra("bundle", bundle);
                intent.putExtra("position", position);

                startActivity(intent);
            }
        });
        dataService = new DataService();
        refreshRestaurantList(dataService);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRestaurantList(dataService);
            }
        });
        return view;
    }

    // Make a async call to get restaurant data.
    private void refreshRestaurantList(DataService dataService) {
        // Initialize the location fields
        if (RestaurantListActivity.location == null) {
            Toast.makeText(getActivity(), "Location Not Available", Toast.LENGTH_LONG).show();
        } else {
            new GetRestaurantsNearbyAsyncTask(this, dataService).execute();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class GetRestaurantsNearbyAsyncTask extends AsyncTask<Void, Void, List<Restaurant>> {

        private Fragment fragment;
        private DataService dataService;
        private Clock clock;

        public GetRestaurantsNearbyAsyncTask(Fragment fragment, DataService dataService) {
            this.fragment = fragment;
            this.dataService = dataService;
            this.clock = new Clock();
            clock.reset();

        }

        @Override
        protected List<Restaurant> doInBackground(Void... params) {
            clock.start();
            // Initialize the location fields
            if (RestaurantListActivity.location == null) {
                return null;
            } else {
                double lat = RestaurantListActivity.location.getLatitude();
                double lng = RestaurantListActivity.location.getLongitude();
                return dataService.getNearbyRestaurants(lat, lng);
            }
        }

        @Override
        protected void onPostExecute(List<Restaurant> restaurants) {
            // Measure the latency of the API call.
            clock.stop();
            Log.e("Latency", Long.toString(clock.getCurrentInterval()));
            if (restaurants != null) {
                super.onPostExecute(restaurants);
                RestaurantAdapter adapter = new RestaurantAdapter(fragment.getActivity(), restaurants);
                gridView.setAdapter(adapter);
                RestaurantListActivity.rests = restaurants;
            } else {
                Toast.makeText(fragment.getActivity(), "Data service error.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
