package com.therandomist.photo_tagger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.service.CategoryService;

public class AddCategoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_categories);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);

        final CategoryService service = new CategoryService(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String categoryName = nameField.getText().toString();
                service.addCategory(new Category(categoryName));
                finish();
            }
        });
    }
}
