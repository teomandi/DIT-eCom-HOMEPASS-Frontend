package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.adapters.PlacesAdapter;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.misc.Utils;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ListView placesContainer;
    private ArrayList<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        placeList = new ArrayList<Place>();
//        placeList.add(new Place(R.drawable.malibu, "California", "Malibu", "Awesome Palace", 24, 8.2f, 3.5f));
//        placeList.add(new Place(R.drawable.tail, "Thailand", "Bangkok", "Nature home", 17, 6f, 2.2f));
//        placeList.add(new Place(R.drawable.athens, "Greece", "Athens", "Xenios Zeus", 44, 13f, 4));
//        placeList.add(new Place(R.drawable.budapest, "Hungary", "Budapest", "Red bed", 16, 9, 3));

        placesContainer = (ListView) view.findViewById(R.id.listview_placescontainer);
        PlacesAdapter adapter = new
                PlacesAdapter(getActivity(), placeList);

        placesContainer.setAdapter(adapter);

        Button rettest = (Button) view.findViewById(R.id.retrofit_test);
        rettest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("RETROFIT TEST starts");
//                String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZW9tYW5kaTQiLCJleHAiOjE1OTc1OTY5MDV9.MS4OMS2oyb0RSZnGRH2_CSup4ffmN3OO7STQ31FQU2ugOSRGxpWdM8-JS6osxQPsQTxmFuQ8xkBfePHzDNg8hw";
//                Retrofit retrofit = RestClient.getClient(token);
//                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//                Call<List<Message>> call = jsonPlaceHolderApi.getAllMessages();
//                call.enqueue(new Callback<List<Message>>() {
//                    @Override
//                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
//                        List<Message> messages = response.body();
//                        for (Message msg: messages){
//                            System.out.println(msg.getBody());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Message>> call, Throwable t) {
//                        System.out.println(t.getMessage());
//                    }
//                });
                Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                List<String> rules = new ArrayList<String>();
                rules.add("please no partying");
                rules.add("please no pets");
                rules.add("please no smoking");
                Call<String> call = jsonPlaceHolderApi.setMultipleRules(rules);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println(response.isSuccessful());
                        System.out.println(response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(
                                getContext(),
                                "Failure on image call!!",
                                Toast.LENGTH_LONG
                        ).show();
                        System.out.println("Error message:: " + t.getMessage());

                    }
                });

            }
        });

        return view;
    }
}
