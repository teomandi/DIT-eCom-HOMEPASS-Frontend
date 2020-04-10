//package com.example.frontmynbnb;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//public class SelectRoleDialog extends DialogFragment {
//
////    public interface MultiChoiceListener{
////        void onPositiveButtonChecked(String[] list, int pos);
////        void onNegativeButtonChecked(String[] list, boolean[] values);
////
////    }
////
////    MultiChoiceListener mListener;
////
////    @Override
////    public void onAttach(Context context){
////        super.onAttach(context);
////        try{
////            mListener = (MultiChoiceListener) context;
////
////        }catch (Exception e){
////            throw new ClassCastException(getActivity().toString() +"MultiChoiceListener must implemented");
////        }
////
////    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        final boolean[] checked = new boolean[2];
////        checked[0] = true;
////        final String[] list = getActivity()/getResources().getStringArray(R.id.roles);
////            builder.setTitle("Select Role")
////                    .setMultiChoiceItems(list, checked, new DialogInterface.OnMultiChoiceClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
////                            checked[which] = isChecked;
////                        }
////                    })
////                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            mListener.onPositiveButtonChecked(list, which);
////                        }
////                    })
////                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////
////                        }
////                    });
////        return builder.create();
//    }
//}
