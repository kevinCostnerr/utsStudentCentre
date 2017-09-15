package com.mad.utsstudcentre.Controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.utsstudcentre.Dialogue.ConfirmDialogue;
import com.mad.utsstudcentre.R;

import java.util.Random;

import butterknife.ButterKnife;

/**
 * CentreFragment is loaded when user selects the Enquiry/sub-enquiry type.
 * This will let user knows the current wait list for each centre to help user to decide the
 * centre they wants to visit.
 */
public class CentreFragment extends Fragment {


    public static final String FINAL_TYPE = "Final Type";
    private static final String TYPE_INDEX = "Final type index";
    public static final String REF_NUMBER = "Reference Number";
    public static final String EST_TIME = "Estimated time";
    public static final String CENTRE_TYPE = "Centre type";
    public static final String CENTRE_01 = "Building 5";
    public static final String CENTRE_02 = "Building 10";
//    public static final int CENTRE_01 = 10;
//    public static final int CENTRE_02 = 20;
    private static final String TAG = "CentreFragment_TAG";
    private String mFinalType;
    private int mFinalTypeIndex;
    private String mRefNumber;
    int mEstTime01;
    int mEstTime02;

    // UI elements
    private LinearLayout mCentre01Layout;
    private LinearLayout mCentre02Layout;
    private TextView mFinalTypeTv;
    private TextView mEst_01Tv;
    private TextView mWait_01Tv;
    private TextView mEst_02Tv;
    private TextView mWait_02Tv;

    public CentreFragment() {
        // Required empty public constructor
    }


    public static CentreFragment newInstance(CharSequence finalType, int index) {
        Bundle args = new Bundle();
        args.putString(FINAL_TYPE, String.valueOf(finalType));
        args.putInt(TYPE_INDEX, index);
        CentreFragment fragment = new CentreFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_centre, container, false);
        if (v != null) {
            ButterKnife.bind(this, v);
        }
        // setting the final type and its index
        mFinalType = getArguments().getString(FINAL_TYPE);
        mFinalTypeIndex = getArguments().getInt(TYPE_INDEX);

        // Binding the UI elements with controller
        mFinalTypeTv = (TextView) v.findViewById(R.id.final_type_tv);
        mEst_01Tv = (TextView) v.findViewById(R.id.est_bd5_tv);
        mEst_02Tv = (TextView) v.findViewById(R.id.est_bd10_tv);
        mWait_01Tv = (TextView) v.findViewById(R.id.waiting_bd5_tv);
        mWait_02Tv = (TextView) v.findViewById(R.id.waiting_bd10_tv);
        initialiseData();

        mFinalTypeTv.setText(mFinalType);

        mCentre01Layout = (LinearLayout) v.findViewById(R.id.centre_01_layout);
        mCentre02Layout = (LinearLayout) v.findViewById(R.id.centre_02_layout);

        // Set OnClickListener to the layout (used as buttons)
        mCentre01Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialogue cfmDialogue = new ConfirmDialogue();
                Bundle bdl = new Bundle();
                bdl.putString(REF_NUMBER, mRefNumber);
                bdl.putString(FINAL_TYPE, mFinalType);
                bdl.putString(CENTRE_TYPE, CENTRE_01);
                bdl.putInt(EST_TIME, mEstTime01);
                Log.d(TAG, "RefNum @ Centre: " + mRefNumber);

                cfmDialogue.setArguments(bdl);
                cfmDialogue.show(getFragmentManager(), "ConfirmDialogue");
            }
        });

        mCentre02Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }


    /**
     * initialiseData set the data for each Student Centre.
     * As we don't have access to the centre's database, the information is generated by random object.
     * The estimated time is calculated out of waiting list. (waiting number * 5 min)
     */
    private void initialiseData() {

        Random random = new Random();
        int wait01 = random.nextInt(20);
        int wait02 = random.nextInt(20);

        mEstTime01 = wait01 * 5;
        mEstTime02 = wait02 * 5;


        String est01 = "";
        String est02 = "";

        if (mEstTime01 > 60) {
            est01 = mEstTime01 / 60 + " hour " + mEstTime01 % 60 + " min";
        } else {
            est01 = mEstTime01 + " min";
        }

        if (mEstTime02 > 60) {
            est02 = mEstTime02 / 60 + " hour " + mEstTime02 % 60 + " min";
        } else {
            est02 = mEstTime02 + " min";
        }

        mEst_01Tv.setText(est01);
        mEst_02Tv.setText(est02);
        mWait_01Tv.setText(wait01 + " people");
        mWait_02Tv.setText(wait02 + " people");

    }

    @Override
    public void onStart() {
        RefNumberAsyncTask async = new RefNumberAsyncTask();
        async.execute();
//        initialiseData();
        super.onStart();
    }

    public void setRefNumber(String refNumber) {
        this.mRefNumber = refNumber;
    }

    /**
     * generate random number for Reference number according to the enquiry type
     */
    private class RefNumberAsyncTask extends AsyncTask<Nullable, String, String> {


        @Override
        protected String doInBackground(Nullable... params) {
            String refNum = "";
            int num;
            Random random = new Random();

            Log.d(TAG, "Centre Async");
            switch (mFinalTypeIndex) {
                case 1001:
                case 1002:
                case 1003:
                    refNum = "D";
                    num = random.nextInt(9999) + 1;
                    // add leading 0s to the ref.number
                    if (num < 10) {
                        refNum += ("000" + num);
                    } else if (num < 100) {
                        refNum += ("00" + num);
                    } else if (num < 1000) {
                        refNum += ("0" + num);
                    }else {
                        refNum += num;
                    }
                    break;
                default:
                    break;
            }

            return refNum;
        }

        @Override
        protected void onPostExecute(String refNum) {
            setRefNumber(refNum);
            super.onPostExecute(refNum);
        }
    }

}
