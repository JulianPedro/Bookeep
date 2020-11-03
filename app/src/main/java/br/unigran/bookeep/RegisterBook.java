package br.unigran.bookeep;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.unigran.bookeep.domain.Book;

public class RegisterBook extends AppCompatActivity {
    private Integer bookID;
    private EditText editName;
    private EditText editTitle;
    private EditText editAuthor;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private EditText editNumPage;
    private EditText editAmount;
    private Button editSave;
    private String eID;

    DatabaseReference databaseBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_edit);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Book");

        editName = findViewById(R.id.editName);
        editTitle = findViewById(R.id.editTitle);
        editAuthor = findViewById(R.id.editAuthor);
        radioGroup = findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        selected = findViewById(selectedId);
        editNumPage = findViewById(R.id.editNumPage);
        editAmount = findViewById(R.id.editAmount);

        editSave = findViewById(R.id.editSave);

        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                selected = findViewById(selectedId);
            }
        });

        if (bundle != null && bundle.get("name") != null) {
            eID = (String) bundle.get("id");
            editName.setText(bundle.get("name").toString());
            editTitle.setText(bundle.get("title").toString());
            editAuthor.setText(bundle.get("author").toString());
            editNumPage.setText(bundle.get("currentPage").toString());
            editAmount.setText(bundle.get("amount").toString());

            System.out.println(bundle.get("status"));

            if (bundle.get("status").equals("Em leitura")) {
                ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            }
            if (bundle.get("status").equals("Finalizado")) {
                ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
            }
            if (bundle.get("status").equals("Não iniciado")) {
                ((RadioButton)radioGroup.getChildAt(2)).setChecked(true);
            }

        }

    }

    private void addBook() {
        String name = editName.getText().toString().trim();
        String title = editTitle.getText().toString().trim();
        String author = editAuthor.getText().toString().trim();
        String page = editNumPage.getText().toString().trim();
        String amount = editAmount.getText().toString().trim();
        String status = (String) selected.getText();
        String id;

        if (!TextUtils.isEmpty(name)) {
            if (eID != null) {
                id = eID;
            } else {
                id = databaseBooks.push().getKey();
            }
            Book book = new Book();
            book.setId(id);
            book.setName(name);
            book.setTitle(title);
            book.setAuthor(author);
            book.setCurrentPage(page);
            book.setAmount(amount);
            book.setStatus(status);

            databaseBooks.child(id).setValue(book);
        }
        super.onBackPressed();
    }
}
