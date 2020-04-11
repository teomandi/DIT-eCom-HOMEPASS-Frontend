package com.example.frontmynbnb;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.zip.Inflater;

public class MessagesFragment extends Fragment {

    TextView mtitle;
    Button addMessage;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messages, container, false);

        mtitle = view.findViewById(R.id.textview_title);
        mtitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChatFragment chatF = ChatFragment.newInstance(10, "Sarah");
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container,
                        new ChatFragment()
                ).addToBackStack(null).commit();
            }
        });

        addMessage = (Button) view.findViewById(R.id.button_addmessage);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout container = (LinearLayout) view.findViewById(R.id.messages_container);
//                Inflater mInfalte = Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        mI.inflate(R.layout.message_component, container, false);
//                container.addView(message);
            }
        });

        return view;
    }
}
