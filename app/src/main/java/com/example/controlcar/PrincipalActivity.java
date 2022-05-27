package com.example.controlcar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

public class PrincipalActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    Wheel wheel;
    Pedals throttlePedal;
    Pedals brakePedal;
    Semnals rightSemnal;
    Semnals leftSemnal;
    Semnals neon;
    Semnals hazardSemnal;
    Semnals playRadio;
    Semnals nightLights;
    volatile boolean connected = false;
    volatile boolean isAlready = false;

    private CommunicateViewModel viewModel;

    int height;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(CommunicateViewModel.class);

        if (!viewModel.setupViewModel(getIntent().getStringExtra("device_name"), getIntent().getStringExtra("device_mac"))) {
            finish();
            return;
        }

        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        //viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.no_messages);
            }
            //messagesView.setText(message);
        });
        viewModel.getMessage().observe(this, message -> {
            // Only update the message if the ViewModel is trying to reset it
            if (TextUtils.isEmpty(message)) {
                //messageBox.setText(message);
            }
        });
        viewModel.connect();

        wheel = new Wheel(this, R.drawable.audi_wheel, viewModel);
        throttlePedal = new Pedals(this, R.drawable.accelerator, viewModel, 'a');
        brakePedal = new Pedals(this, R.drawable.brake_pedal, viewModel, 'b');
        rightSemnal = new Semnals(this, R.drawable.ic_baseline_arrow_circle_right_24, 1, viewModel);
        leftSemnal = new Semnals(this, R.drawable.ic_baseline_arrow_circle_left_24, -1, viewModel);
        playRadio = new Semnals(this, R.drawable.ic_baseline_play_circle_24, 2, viewModel);
        nightLights = new Semnals(this, R.drawable.ic_baseline_lightbulb_circle_24, 3, viewModel);
        neon = new Semnals(this, R.drawable.ic_round_flare_24, 4, viewModel);
        hazardSemnal = new Semnals(this, R.drawable.hazard_lights2, 0, viewModel);
    }

    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                connected = true;
                if (!isAlready){
                    isAlready = true;
                    addAcceleratorPedal();
                    addBrakePedal();
                    addRotatingWheel();
                    addLeftSemnal();
                    addRightSemnal();
                    addHazardSemnal();
                    addPlaySemnal();
                    addNightLightsSemnal();
                    addNeonSemnal();
                }
                break;

            case CONNECTING:
                //
                break;

            case DISCONNECTED:
                finish();
                break;
        }
    }


    private void addRotatingWheel() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.setMargins(0, (int) (height / 2.5), (int) (width / 1.5), 0);
        params.height = relativeLayout.getMeasuredHeight() / 2;
        //params.width = relativeLayout.getWidth() / 2;
        wheel.setLayoutParams(params);

        relativeLayout.addView(wheel);
    }

    private void addAcceleratorPedal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.setMargins(0, (int) (height / 2.5), -(int) (width / 1.2), 0);
        params.height = relativeLayout.getMeasuredHeight() / 2;
        //params.width = relativeLayout.getWidth() / 2;
        throttlePedal.setLayoutParams(params);

        relativeLayout.addView(throttlePedal);
    }

    private void addBrakePedal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.height = relativeLayout.getMeasuredHeight() / 5;
        params.width = (int) (relativeLayout.getWidth() / 4);
        params.setMargins((int) (width / 1.64), (int) (height - height / 3.2), 0, 0);

        brakePedal.setLayoutParams(params);

        relativeLayout.addView(brakePedal);
    }


    private void addRightSemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        rightSemnal.setSemnals(leftSemnal, rightSemnal, hazardSemnal);

        params.height = (int)(relativeLayout.getMeasuredHeight() / 3.5);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (4 * width / 5) ,0, 0, (int) (height / 2));

        rightSemnal.setLayoutParams(params);

        relativeLayout.addView(rightSemnal);
    }

    private void addLeftSemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        leftSemnal.setSemnals(leftSemnal, rightSemnal, hazardSemnal);
        params.height = (int)(relativeLayout.getMeasuredHeight() / 3.5);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (4 * width / 6) ,0, 0, (int) (height / 2));

        leftSemnal.setLayoutParams(params);

        relativeLayout.addView(leftSemnal);
    }


    private void addHazardSemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        hazardSemnal.setSemnals(leftSemnal, rightSemnal, hazardSemnal);
        params.height = (int)(relativeLayout.getMeasuredHeight() / 6);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (16.9 * width / 23) ,(int) (height / 3.5), 0, 0);

        hazardSemnal.setLayoutParams(params);
        relativeLayout.addView(hazardSemnal);
    }

    private void addPlaySemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.height = (int)(relativeLayout.getMeasuredHeight() / 3.5);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (width / 18) ,0, 0 , (int) (height / 2));

        playRadio.setLayoutParams(params);
        relativeLayout.addView(playRadio);
    }


    private void addNightLightsSemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.height = (int)(relativeLayout.getMeasuredHeight() / 3.5);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (2.07* width / 11) ,0, 0, (int) (height / 2));

        nightLights.setLayoutParams(params);

        relativeLayout.addView(nightLights);
    }

    private void addNeonSemnal() {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.height = (int)(relativeLayout.getMeasuredHeight() / 3.5);
        params.width = (int) (relativeLayout.getWidth() / 8);
        params.setMargins((int) (3.70* width / 11) ,0, 0, (int) (height / 2));

        neon.setLayoutParams(params);

        relativeLayout.addView(neon);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        relativeLayout = (RelativeLayout) findViewById(R.id.car_layout);

        height = relativeLayout.getMeasuredHeight();
        width = relativeLayout.getMeasuredWidth();

        if (connected) {
            if (!isAlready){
                isAlready = true;
                addAcceleratorPedal();
                addBrakePedal();
                addRotatingWheel();
                addLeftSemnal();
                addRightSemnal();
                addHazardSemnal();
                addPlaySemnal();
                addNightLightsSemnal();
                addNeonSemnal();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
