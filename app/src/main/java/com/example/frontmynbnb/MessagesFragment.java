package com.example.frontmynbnb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.Objects;

public class MessagesFragment extends Fragment {

    TextView mtitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mtitle = view.findViewById(R.id.textview_title);
        mtitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Switching fragment", Toast.LENGTH_SHORT).show();

                ChatFragment chatF = ChatFragment.newInstance(10, "Sarah");

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container,
                        new ChatFragment()
                ).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
