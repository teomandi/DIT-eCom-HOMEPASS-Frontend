package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.activities.HostActivity;
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.fragments.ChatFragment;
import com.example.frontmynbnb.models.Message;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends ArrayAdapter<Message> {

    public MessagesAdapter(Context ctx, List<Message> messages, boolean isChat){
        super(ctx, 0, messages);
        mContext = ctx;
        mIsChat = isChat;
    }
    private TextView mTextUsername, mTextText;
    private CircleImageView mImageViewProfile;
    private Context mContext;
    private boolean mIsChat;
    private String username =null;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final Message mMessage = getItem(position);

        if(view == null){
            if(mIsChat) {
                if(mMessage.getSender().getId() == AppConstants.USER.getId()) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.mymessage_component, parent, false);
                }else{
                    view = LayoutInflater.from(getContext()).inflate(R.layout.message_component, parent, false);
                }
            }else{
                view = LayoutInflater.from(getContext()).inflate(R.layout.message_component, parent, false);
            }
        }
        username = mIsChat ? mMessage.getSender().getUsername() :
                mMessage.getSender().getId() == AppConstants.USER.getId() ?
                        mMessage.getReciever().getUsername() : mMessage.getSender().getUsername();

        mTextUsername = (TextView) view.findViewById(R.id.textview_chat_username);
        mTextText = (TextView) view.findViewById(R.id.textview_chat_text);
        mImageViewProfile = (CircleImageView) view.findViewById(R.id.imageview_chat_profile);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsChat)
                    return;
                int otherUserId = mMessage.getSender().getId() == AppConstants.USER.getId() ?
                        mMessage.getReciever().getId() : mMessage.getSender().getId();
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("other_user_id", otherUserId);
                System.out.println("other user is  " + otherUserId);
                chatFragment.setArguments(bundle);
                if (AppConstants.MODE.equals("GUEST")) {
                    ((MainActivity) mContext).getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragment_container,
                            chatFragment
                    ).addToBackStack(null).commit();
                } else {
                    ((HostActivity) mContext).getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragment_container2,
                            chatFragment
                    ).addToBackStack(null).commit();
                }
            }
        });

        mTextUsername.setText(username);
        mTextText.setText(mMessage.getText());
        if(mMessage.getUserImage() != null)
            mImageViewProfile.setImageBitmap(mMessage.getUserImage());

        return view;
    }
}
