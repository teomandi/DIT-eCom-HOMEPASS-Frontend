package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends ArrayAdapter<Message> {

    public MessagesAdapter(Context ctx, List<Message> messages){
        super(ctx, 0, messages);
    }
    private TextView mTextUsername, mTextText;
    private CircleImageView mImageViewProfile;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_component, parent, false);
        }

        final Message message = getItem(position);
        mTextUsername = (TextView) view.findViewById(R.id.textview_chat_username);
        mTextText = (TextView) view.findViewById(R.id.textview_chat_text);
        mImageViewProfile = (CircleImageView) view.findViewById(R.id.imageview_chat_profile);

        String username = message.getSender().getId() == AppConstants.USER.getId() ?
                message.getReciever().getUsername() : message.getSender().getUsername();
        System.out.println("Username:: " + username);
        mTextUsername.setText(username);
        mTextText.setText(message.getText());
        if(message.getUserImage() != null)
            mImageViewProfile.setImageBitmap(message.getUserImage());

        return view;
    }
}
