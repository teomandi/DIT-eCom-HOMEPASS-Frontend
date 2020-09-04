package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.activities.HostActivity;
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.adapters.MessagesAdapter;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessagesFragment extends MyFragment {

    private ListView mMessagesContainer;
    private MessagesAdapter mMesAdapter;
    private List<Message> mMessagesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mMessagesContainer = view.findViewById(R.id.listview_messages_container);
        mMessagesList = new ArrayList<>();
        mMesAdapter = new MessagesAdapter(getContext(), mMessagesList);
        mMessagesContainer.setAdapter(mMesAdapter);
        if(AppConstants.MODE.equals("GUEST"))
            MainActivity.setBottomNavChecked(0);
        else {
            HostActivity.setBottomNavChecked(0);
            fetchMessages(true);
        }
            return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("MESSAGES");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                AppConstants.MODE.equals("GUEST") ? R.id.fragment_container : R.id.fragment_container2,
                AppConstants.MODE.equals("GUEST") ? new HomeFragment() : new PlaceFragment()
        ).addToBackStack(null).commit();
        return true;
    }

    private void fetchMessages(boolean host){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Message>> call = host ? jsonPlaceHolderApi.getMessagesAsHost() : jsonPlaceHolderApi.getMessagesAsGuest();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                mMessagesList.addAll(response.body());
                mMesAdapter.notifyDataSetChanged();
                for(Message message: mMessagesList){
                    if(message.getSender().getId() == AppConstants.USER.getId()){
                        //fetch the reciever the image
                        fetchUserImage(message.getReciever(), message);
                    }
                    else{
                        //fetch the sender the image
                        fetchUserImage(message.getSender(), message);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on search!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }

    private void fetchUserImage(User targetUser, final Message message){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<ResponseBody> call = jsonPlaceHolderApi.getUserImage(targetUser.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                byte[] imageBytes = new byte[0];
                try {
                    imageBytes = response.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert imageBytes != null;
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                message.setUserImage(imageBitmap);
                mMesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }


    //chat fragment
//    mTextTitle.setOnClickListener(new View.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            ChatFragment chatF = ChatFragment.newInstance(10, "Sarah");
//            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
//                    R.id.fragment_container,
//                    new ChatFragment()
//            ).addToBackStack(null).commit();
//        }
//    });

}
