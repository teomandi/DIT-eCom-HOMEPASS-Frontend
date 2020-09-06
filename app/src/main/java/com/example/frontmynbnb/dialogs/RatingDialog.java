package com.example.frontmynbnb.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.models.Rating;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RatingDialog extends AppCompatDialogFragment {

    private TextView mTextAddress;
    private RatingBar mRatingBar;
    private EditText mEditComment;
    private int mPlaceId;
    private String mPlaceAddress;
    private RatingDialogListener listener;

    public static RatingDialog newInstance(int pid, String address) {
        RatingDialog f = new RatingDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("place_address", address);
        args.putInt("place_id", pid);

        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rate_dialog, null);

        builder.setView(view)
                .setTitle("Rate Place")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = mEditComment.getText().toString();
                        float degree = mRatingBar.getRating();
                        Rating rating = new Rating(degree, comment);
                        listener.applyRatings(rating, mPlaceId);
                        System.out.println("OK!! Sending rating!");
                    }
        });
        mPlaceAddress = getArguments().getString("place_address");
        mPlaceId =getArguments().getInt("place_id");

        mTextAddress = view.findViewById(R.id.textview_ratedialog_placeaddress);
        mTextAddress.setText(mPlaceAddress);
        mEditComment = view.findViewById(R.id.edittext_ratingdialog_comment);
        mRatingBar = view.findViewById(R.id.ratingbar_dialog_rate);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RatingDialogListener)context;
        } catch ( ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RatingDialogListener");
        }
    }

    public interface RatingDialogListener{
        void applyRatings(Rating rating, int placeid);
    }


}
