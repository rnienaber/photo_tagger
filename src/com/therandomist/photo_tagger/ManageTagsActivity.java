package com.therandomist.photo_tagger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.CategoryService;
import com.therandomist.photo_tagger.service.TagService;

import java.util.List;

public class ManageTagsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_tags);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);

        final CategoryService categoryService = new CategoryService(this);
        final TagService tagService = new TagService(this);

        final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        List<Category> categories = categoryService.getAllCategories();

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(adapter);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Category category = (Category)spinner.getSelectedItem();
                String tagName = nameField.getText().toString();
                tagService.addTag(new Tag(tagName, category));
                finish();
            }
        });
    }
}
