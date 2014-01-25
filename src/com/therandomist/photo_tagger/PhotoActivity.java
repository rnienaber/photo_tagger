package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;
import com.therandomist.photo_tagger.service.PhotoService;

public class PhotoActivity extends Activity {

    private Photo photo = null;
    private PhotoService photoService;
    private String photoPath = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoService = new PhotoService(getApplicationContext());

        setContentView(R.layout.photo);
        readFromBundle();
        loadPhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhoto();
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        photoPath = (String)bundle.get("photoPath");
    }

    public void loadPhoto(){
        photo = photoService.getPhoto(photoPath);

        ImageView photoView = (ImageView) findViewById(R.id.photo);
        if(photoView != null){
            BitmapDrawable d = new BitmapDrawable(getResources(), photoPath);
            photoView.setImageDrawable(d);
        }

        TextView photoNameView = (TextView) findViewById(R.id.photo_file_name);
        if(photoNameView != null){
            photoNameView.setText(photoPath.replace(FileHelper.ROOT+"/", ""));
        }

        loadPeopleTags();
        loadPrintingTags();
        loadKeywordTags();
        loadLocationText();
        loadNotes();
    }
    
    public void loadPeopleTags(){
        TextView peopleTagsView = (TextView) findViewById(R.id.people_tags_text);
        if(peopleTagsView != null){
            peopleTagsView.setText(photo.getPeople());
        }
    }

    public void loadPrintingTags(){
        TextView printingTagsView = (TextView) findViewById(R.id.printing_tags_text);
        if(printingTagsView != null){
            printingTagsView.setText(photo.getPrinting());
        }
    }

    public void loadKeywordTags(){
        TextView keywordTagsView = (TextView) findViewById(R.id.keyword_tags_text);
        if(keywordTagsView != null){
            keywordTagsView.setText(photo.getKeywords());
        }
    }

    public void loadLocationText(){
        TextView locationTextView = (TextView) findViewById(R.id.location_text);
        if(locationTextView != null){
            locationTextView.setText(photo.getLocationName());
        }
    }

    public void loadNotes(){
        TextView notesTextView = (TextView) findViewById(R.id.notes_text);
        if(notesTextView != null){
            notesTextView.setText(photo.getNotes());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_people:
                return handleOptionsItemSelected(PhotoTagListActivity.class, "people");
            case R.id.add_keywords:
                return handleOptionsItemSelected(PhotoTagListActivity.class, "keywords");
            case R.id.set_printing:
                return handleOptionsItemSelected(PhotoTagListActivity.class, "printing");
            case R.id.set_location:
                Intent i = new Intent(getApplicationContext(), ManageCountriesActivity.class);
                i.putExtra("state", "photo");
                startActivityForResult(i, ManageCountriesActivity.IDENTIFIER);
                return true;
            case R.id.add_notes:
                Intent ni = new Intent(getApplicationContext(), AddNotesActivity.class);
                ni.putExtra("photoPath", photoPath);
                startActivity(ni);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(HomeActivity.APP_NAME, "Manage Photo - Returning from: "+requestCode);

        switch(requestCode){
            case ManageCountriesActivity.IDENTIFIER :
                if (resultCode == Activity.RESULT_OK){
                    String name = data.getStringExtra("name");
                    Double latitude = data.getDoubleExtra("latitude", 0);
                    Double longitude = data.getDoubleExtra("longitude", 0);

                    Location location = new Location(name, latitude, longitude, null);
                    photo.setLocation(location);
                    photoService.savePhoto(photo);
                    loadLocationText();
                }
                break;
        }
    }

    public boolean handleOptionsItemSelected(Class klass, String categoryName){
        Intent i = new Intent(getApplicationContext(), klass);
        i.putExtra("categoryName", categoryName);
        i.putExtra("photoPath", photoPath);
        startActivity(i);
        return true;
    }
}