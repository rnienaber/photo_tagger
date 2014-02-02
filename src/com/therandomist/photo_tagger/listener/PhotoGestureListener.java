package com.therandomist.photo_tagger.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.therandomist.photo_tagger.PhotoActivity;
import com.therandomist.photo_tagger.R;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;

public class PhotoGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "TAGGER GESTURES";
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    String previousPhotoPath;
    String nextPhotoPath;
    Activity parentActivity;

    public PhotoGestureListener(Activity parentActivity, Photo photo) {
        this.parentActivity = parentActivity;
        String[] previousAndNextPaths = FileHelper.getPreviousAndNextPaths(photo.getFolder(), photo.getFilename());
        previousPhotoPath = previousAndNextPaths[0];
        nextPhotoPath = previousAndNextPaths[1];
    }

    private void startPhotoActivity(String path, boolean previous){
        if(path != null){
            Intent i = new Intent(parentActivity, PhotoActivity.class);
            i.putExtra("photoPath", path);
            parentActivity.startActivity(i);

            if(previous){
                parentActivity.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            }

            parentActivity.finish();
        }
    }

    private void onSwipeRight(){
        startPhotoActivity(previousPhotoPath, true);
    }

    private void onSwipeLeft(){
        startPhotoActivity(nextPhotoPath, false);
    }

    private void onSwipeTop(){}

    private void onSwipeBottom(){}

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        try {
            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }
}