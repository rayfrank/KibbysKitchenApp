package com.kibbyskitchen.kibbyskitchenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kibbyskitchen.kibbyskitchenapp.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout dynamically
        setContentView(R.layout.intro_acitvity);

        TextView textView = findViewById(R.id.textView);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        // Load the animation
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply the animation to the TextView
        textView.startAnimation(slideUpAnimation);

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start, no action needed
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation end, adjust view position
                textView.clearAnimation(); // Clear the animation to prevent the view from reverting to its original position
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.topMargin -= textView.getHeight(); // Adjust the top margin by subtracting the view height
                textView.setLayoutParams(layoutParams); // Apply the new layout parameters
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat, no action needed
            }
        });

        // Simulate loading for 2 seconds
        new Handler().postDelayed(() -> {
            // Launch MainActivity
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);

            // Hide progress bar after launching MainActivity
            progressBar.setVisibility(View.GONE);
        }, 2000); // Adjust delay as needed
    }
}
