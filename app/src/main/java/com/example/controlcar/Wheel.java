package com.example.controlcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

@SuppressLint("ViewConstructor")
public class Wheel extends AppCompatImageView {
    private CommunicateViewModel viewModel;
    int direction = 0;
    int degree = 0;
    int old_degree;
    private float centerX;
    private float centerY;

    public Wheel(Context context, int res, CommunicateViewModel viewModel) {
        super(context);
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
        float newX;
        float newY;
        float x;
        float y;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            x = event.getX();
            y = event.getY();

            newX = centerX - x;
            newY = centerY - y;

            updateRotation(newX, newY);

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            x = event.getX();
            y = event.getY();

            newX = centerX - x;
            newY = centerY - y;

            updateRotation(newX, newY);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            x = event.getX();
            y = event.getY();

            newX = centerX - x;
            newY = centerY - y;
            setDirection(0);

            System.out.println(0);
            viewModel.sendMessage(String.valueOf((char)(49)));
        }
        return true;
    }

    private void updateRotation(float newX2, float newY2) {
        degree = (int) Math.toDegrees(Math.atan2(newY2, newX2)) - 90;

        if (degree >= -90 && degree <= 90) {

            setDirection(degree);

            //System.out.println(degree);
            if (degree / 10 + 10 != old_degree / 10 + 10) {
                System.out.println(degree / 10 + 50);
                viewModel.sendMessage(String.valueOf((char)(degree / 10 + 50)));
            }
            old_degree = degree;
        }
    }
}
