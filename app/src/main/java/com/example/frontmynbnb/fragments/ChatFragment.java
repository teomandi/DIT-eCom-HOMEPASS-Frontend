package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
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

public class ChatFragment extends MyFragment {

    private EditText mEditMessage;
    private Button mButtonSend;
    private ListView mMessegeContainer;
    private MessagesAdapter mMesAdapter;
    private List<Message> mMessagesList;
    private int mOtherUserId;
    private Bitmap mBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mOtherUserId = getArguments().getInt("other_user_id");
        System.out.println("otherUserId: " + mOtherUserId);

        mEditMessage = view.findViewById(R.id.edittext_chat_messagetext);
        mMessegeContainer = view.findViewById(R.id.listview_chat_messagescontainer);
        mMessagesList = new ArrayList<>();
        mMesAdapter = new MessagesAdapter(getContext(), mMessagesList, true);
        mMessegeContainer.setAdapter(mMesAdapter);
        mButtonSend = view.findViewById(R.id.button_chat_sendmessage);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditMessage.getText().toString();
                if(message.equals("")){
                    Toast.makeText(getContext(), "Text is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                postMessage(message);
            }
        });
        if(AppConstants.MODE.equals("HOST"))
            mButtonSend.setBackground(getContext().getDrawable(R.drawable.success_button));
        fetchChatMesseges();
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("CHAT");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                AppConstants.MODE.equals("GUEST") ? R.id.fragment_container : R.id.fragment_container2,
                AppConstants.MODE.equals("GUEST") ? new MessagesFragment() : new MessagesFragment()
        ).addToBackStack(null).commit();
        return true;
    }

    private void fetchChatMesseges() {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Message>> call = AppConstants.MODE.equals("GUEST") ?
                jsonPlaceHolderApi.getChatNotAsHost(AppConstants.USER.getId(), mOtherUserId)
                : jsonPlaceHolderApi.getChatAsHost(AppConstants.USER.getId(), mOtherUserId);
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
                fetchUserImage(mOtherUserId);
                fetchUserImage(AppConstants.USER.getId());
                mMessegeContainer.smoothScrollToPosition(mMesAdapter.getCount());

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


    private void fetchUserImage(final int targetUserId) {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<ResponseBody> call = jsonPlaceHolderApi.getUserImage(targetUserId);
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
                for (Message m : mMessagesList) {
                    if (m.getSender().getId() == targetUserId) {
                        m.setUserImage(imageBitmap);
                        if(targetUserId == AppConstants.USER.getId())
                            mBitmap = imageBitmap;
                        mMesAdapter.notifyDataSetChanged();

                    }
                }
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

    private void postMessage(String content){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Message> call = jsonPlaceHolderApi.postMessage(
                AppConstants.USER.getId(),
                mOtherUserId,
                AppConstants.MODE.equals("GUEST") ? mOtherUserId : AppConstants.USER.getId(),
                content
        );
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                mEditMessage.setText("");
                Message mymsg = response.body();
                mymsg.setUserImage(mBitmap);
                mMessagesList.add(mymsg);
                mMesAdapter.notifyDataSetChanged();
//                mMessegeContainer.setAdapter(mMesAdapter);
                mMessegeContainer.smoothScrollToPosition(mMesAdapter.getCount());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }
}
