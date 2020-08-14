package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.activities.HostActivity;
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.R;

import java.util.Objects;

public class MessagesFragment extends MyFragment {

    TextView mTextTitle;
    Button mButtonAddMessage;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messages, container, false);

        mTextTitle = view.findViewById(R.id.textview_title);
        mTextTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChatFragment chatF = ChatFragment.newInstance(10, "Sarah");
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container,
                        new ChatFragment()
                ).addToBackStack(null).commit();
            }
        });

        mButtonAddMessage = (Button) view.findViewById(R.id.button_addmessage);
        if(AppConstants.MODE.equals("HOST"))
            mButtonAddMessage.setBackgroundResource(R.drawable.success_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout container = (LinearLayout) view.findViewById(R.id.messages_container);
//                Inflater mInfalte = Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        mI.inflate(R.layout.message_component, container, false);
//                container.addView(message);
            }
        });


        if(AppConstants.MODE.equals("GUEST"))
            MainActivity.setBottomNavChecked(0);
        else
            HostActivity.setBottomNavChecked(0);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("MESSAGES");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                AppConstants.MODE.equals("GUEST") ? R.id.fragment_container : R.id.fragment_container2,
                AppConstants.MODE.equals("GUEST") ? new HomeFragment() : new HostFragment()
        ).addToBackStack(null).commit();
        return true;
    }

}
