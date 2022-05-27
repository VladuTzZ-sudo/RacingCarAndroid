package com.example.controlcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

@SuppressLint("ViewConstructor")
public class Semnals extends AppCompatImageView {
    private CommunicateViewModel viewModel;
    int direction = 0;
    private final int res;
    private final int mode;
    boolean hold;
    private Semnals left;
    private Semnals right;
    private Semnals hazard;

    public void setSemnals(Semnals left, Semnals right, Semnals hazard) {
        this.left = left;
        this.right = right;
        this.hazard = hazard;
    }

    public Semnals(Context context, int res, int mode, CommunicateViewModel viewModel) {
        super(context);
        this.res = res;
        this.viewModel = viewModel;
        this.setImageResource(res);
        this.mode = mode;
        if (mode == 4) {
            this.setScaleX(0.8f);
            this.setScaleY(0.8f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = this.getHeight();
        int width = this.getWidth();
        float centerX = (int) (width / 2);
        float centerY = (int) (height / 2);
        canvas.rotate(direction, centerX, centerY);
        super.onDraw(canvas);
    }

    public void forceOff() {
        this.setImageResource(res);
        this.hold = false;
    }

    public void forceOn() {
        if (mode == -1) {
            this.setImageResource(R.drawable.left_color);
            this.hold = true;
        } else if (mode == 1) {
            this.setImageResource(R.drawable.right_color);
            this.hold = true;
        } else if (mode == 0) {
            this.hold = true;
            this.setImageResource(R.drawable.hazard_lights2_colored);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            updateRotation();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            updateRotation();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.setScaleX(1.0f);
            this.setScaleY(1.0f);

            if (!hold) {
                hold = true;
                if (mode == -1) {
                    //left on
                    viewModel.sendMessage(String.valueOf((char)(0)));
                    if (!hazard.hold && right.hold) {
                        hazard.forceOn();
                    }
                    this.setImageResource(R.drawable.left_color);
                } else if (mode == 1) {
                    //right on
                    viewModel.sendMessage(String.valueOf((char)(1)));
                    if (!hazard.hold && left.hold) {
                        hazard.forceOn();
                    }
                    this.setImageResource(R.drawable.right_color);
                } else if (mode == 0) {
                    //hazard on
                    left.forceOn();
                    right.forceOn();
                    viewModel.sendMessage(String.valueOf((char)(2)));
                    this.setImageResource(R.drawable.hazard_lights2_colored);
                } else if (mode == 2) {
                    //music
                    viewModel.sendMessage(String.valueOf((char)(3)));
                    this.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                } else if (mode == 3) {
                    //flash
                    viewModel.sendMessage(String.valueOf((char)(4)));
                    this.setImageResource(R.drawable.ic_baseline_flashlight_off_24);
                    this.setScaleX(0.8f);
                    this.setScaleY(0.8f);
                } else if (mode == 4) {
                    //neon
                    viewModel.sendMessage(String.valueOf((char)(5)));
                    this.setImageResource(R.drawable.ic_baseline_blur_off_24);
                    this.setScaleX(0.8f);
                    this.setScaleY(0.8f);
                }
            } else {
                hold = false;
                if (mode == -1) {
                    //left
                    viewModel.sendMessage(String.valueOf((char)(6)));
                    if (hazard.hold) {
                        hazard.forceOff();
                    }
                } else if (mode == 1) {
                    //right
                    viewModel.sendMessage(String.valueOf((char)(7)));
                    if (hazard.hold) {
                        hazard.forceOff();
                    }
                } else if (mode == 0) {
                    //hazard
                    viewModel.sendMessage(String.valueOf((char)(8)));
                    left.forceOff();
                    right.forceOff();
                } else if (mode == 2) {
                    //music
                    viewModel.sendMessage(String.valueOf((char)(9)));
                } else if (mode == 3) {
                    //flash
                    viewModel.sendMessage(String.valueOf((char)(10)));
                } else if (mode == 4) {
                    //neon
                    viewModel.sendMessage(String.valueOf((char)(11)));
                }
                this.setImageResource(res);
            }
        }
        return true;
    }

    private void updateRotation() {
        this.setScaleX(0.75f);
        this.setScaleY(0.75f);
    }

}