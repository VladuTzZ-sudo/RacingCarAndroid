package com.example.controlcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

@SuppressLint("ViewConstructor")
public class Pedals extends AppCompatImageView {
    private CommunicateViewModel viewModel;
    int direction = 0;
    private float centerX;
    private float centerY;
    private final int res;
    private char type;
    private int oldSend;
    private boolean pressed = false;

    public Pedals(Context context, int res, CommunicateViewModel viewModel, char type) {
        super(context);
        this.res = res;
        this.type = type;
        this.viewModel = viewModel;
        this.setImageResource(res);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int height = this.getHeight();
        int width = this.getWidth();
        centerX = (int) (width / 2);
        centerY = (int) (height / 2);
        canvas.rotate(direction, centerX, centerY);
        super.onDraw(canvas);

    }

    public void setDirection(int direction) {
        this.direction = direction;
        this.invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float newY;
        float newX;
        float y;
        float x;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();

            newX = -centerX + x;
            newY = -centerY + y;

            updateRotation(newX, newY);

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            x = event.getX();
            y = event.getY();

            newX = -centerX + x;
            newY = -centerY + y;

            updateRotation(newX, newY);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            x = event.getX();
            y = event.getY();

            newX = -centerX + x;
            newY = -centerY + y;

            if (res == R.drawable.accelerator) {
                updateRotation(newX, centerY);
            } else {
                //break off
                this.setScaleX(1.0f);
                this.setScaleY(1.0f);
                viewModel.sendMessage(String.valueOf((char)(13)));
            }
        }
        return true;
    }

    private void updateRotation(float newX2, float newY2) {
        float scale;
        if (newY2 > 0) {
            scale = (Math.min(newY2 / centerY, 1) + 1) / 2;
        } else {
            scale = (Math.max(newY2 / centerY + 1, 0.f)) / 2;
        }

        //System.out.println(100 - (int)(scale * 100) + " " + oldSend + " " + (char)(100 - (int)(scale * 100)));

        if (type == 'a') {
            //System.out.println("a" + (100 - (int) (scale * 100))/10);
            int x = (100 - (int) (scale * 100))/10;
            if (x != oldSend) {
                viewModel.sendMessage(String.valueOf((char)(x + 20)));
                System.out.println(x);
                oldSend = x;
            }
        }

        if (res == R.drawable.accelerator) {
            this.setScaleY(scale / 4 + 0.80f);
            setDirection((int) ((1 - scale) * -3));
        } else {
            //break
            viewModel.sendMessage(String.valueOf((char)(12)));

            this.setScaleX(0.75f);
            this.setScaleY(0.75f);
        }
    }

}