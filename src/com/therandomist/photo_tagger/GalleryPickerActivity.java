package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.FileHelper;
import com.therandomist.photo_tagger.service.TagService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryPickerActivity extends Activity {

    private Map<String, List<Tag>> lookupMap = new HashMap<String, List<Tag>>();
    private TagService tagService;
    private String folder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_picker);

        this.tagService = new TagService(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        folder = FileHelper.getCorrectedFolder((String)bundle.get("folder"));

        TextView photoNameView = (TextView) findViewById(R.id.folder_name);
        if(photoNameView != null){
            photoNameView.setText(folder);
        }

        Button showButton = (Button) findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                handleShowPhotos();
            }
        });
    }

    private void displaySelectedTags(){
        TextView peopleTagsView = (TextView) findViewById(R.id.people_tags_text);
        if (peopleTagsView != null) {
            List<Tag> peopleTags = lookupMap.get("people");
            peopleTagsView.setText(Tag.getString(peopleTags));
        }
        TextView printingTagsView = (TextView) findViewById(R.id.printing_tags_text);
        if (printingTagsView != null) {
            List<Tag> printingTags = lookupMap.get("printing");
            printingTagsView.setText(Tag.getString(printingTags));
        }
        TextView keywordTagsView = (TextView) findViewById(R.id.keyword_tags_text);
        if (keywordTagsView != null) {
            List<Tag> keywordTags = lookupMap.get("keywords");
            keywordTagsView.setText(Tag.getString(keywordTags));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_picker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_people:
                return handleOptionsItemSelected(TagPickerActivity.class, "people");
            case R.id.add_keywords:
                return handleOptionsItemSelected(TagPickerActivity.class, "keywords");
            case R.id.set_printing:
                return handleOptionsItemSelected(TagPickerActivity.class, "printing");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleShowPhotos(){
        Intent i = new Intent(this, GalleryActivity.class);

        for(Map.Entry<String, List<Tag>> entry : lookupMap.entrySet()){
            String tagString = Tag.getString(entry.getValue());
            i.putExtra(entry.getKey(), tagString);
        }

        i.putExtra("folder", folder);

        startActivity(i);
    }

    private boolean handleOptionsItemSelected(Class klass, String categoryName) {
        Intent i = new Intent(getApplicationContext(), klass);
        i.putExtra("categoryName", categoryName);

        List<Tag> selectedTags = lookupMap.get(categoryName);
        int[] ids = Tag.getIds(selectedTags);

        i.putExtra("selectedTagIds", ids);
        startActivityForResult(i, TagPickerActivity.IDENTIFIER);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TagPickerActivity.IDENTIFIER:
                handleReturnFromTagPicker(resultCode, data);
                break;
        }
    }

    private void handleReturnFromTagPicker(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String categoryName = data.getStringExtra("categoryName");
            int[] selectedTagIds = data.getIntArrayExtra("selectedTagsIds");
            List<Tag> selectedTags = tagService.getTagsById(selectedTagIds);

            lookupMap.put(categoryName, selectedTags);
            displaySelectedTags();
        }
    }
}