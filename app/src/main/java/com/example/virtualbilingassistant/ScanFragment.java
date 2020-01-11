package com.example.virtualbilingassistant;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment {

    SurfaceView cameraView;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    LinearLayout List1;
    ImageButton addCart;
    BottomNavigationView btmnav;
    final int RequestCameraPermissionID=1;
    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_scan, container, false);
        cameraView=view.findViewById(R.id.surfaceView);
        txtResult=view.findViewById(R.id.Result);
        barcodeDetector=new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(getActivity(), barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(640,480).build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    //If permission Granted then start Camera
                    cameraSource.start(cameraView.getHolder());
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }

        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> sparseArray=detections.getDetectedItems();
                if (sparseArray.size() != 0)
                {
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Vibrator vibrator=(Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            //vibrator.vibrate(10);//here give the time in MilliSeconds
                            //txtResult.setVisibility(View.GONE);
                            btmnav = getActivity().findViewById(R.id.main_nav);
                            btmnav.setVisibility(View.GONE);
                            txtResult.setText(sparseArray.valueAt(0).displayValue);
                            final TextView t = new TextView(getActivity());
                            List1 = view.findViewById(R.id.List);
                            addCart =view.findViewById(R.id.addCart);
                            addCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                    t.setText(sparseArray.valueAt(0).displayValue);
                                    t.setBackgroundResource(R.drawable.textview_border);
                                    t.setTextAppearance(R.style.fontForCartItem);
                                    List1.addView(t);
                                    Toast.makeText(getActivity(),"Added Product",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });


                }
            }
        });
        return view;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case RequestCameraPermissionID:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    try {
                        //If permission Granted then start Camera
                        cameraSource.start(cameraView.getHolder());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}
