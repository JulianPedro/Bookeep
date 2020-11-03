package br.unigran.bookeep;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.unigran.bookeep.domain.Book;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Book> books;
    private Context context;

    public BookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_books,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookName.setText(books.get(position).getName());
        holder.bookAuthor.setText(books.get(position).getAuthor());
        holder.itemRemove.setOnClickListener(new RemoveListener(books.get(position).getId()));
        holder.itemEdit.setOnClickListener(new EditListener(books.get(position)));
    }

    private class EditListener implements View.OnClickListener {
        private Book book;

        public EditListener(Book book) {
            this.book = book;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RegisterBook.class);
            intent.putExtra("id", book.getId());
            intent.putExtra("name", book.getName());
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("currentPage", book.getCurrentPage());
            intent.putExtra("amount", book.getAmount());
            intent.putExtra("status", book.getStatus());
            context.startActivity(intent);
        }
    }

    private class RemoveListener implements View.OnClickListener {
        private String id;

        public RemoveListener(String id) {
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Book");
            ref.child(String.valueOf(id)).removeValue();
        }
    }

    @Override
    public int getItemCount() {
        if (books != null) {
            return books.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView bookAuthor;
        Button itemRemove;
        Button itemEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            itemRemove = itemView.findViewById(R.id.itemRemove);
            itemEdit = itemView.findViewById(R.id.itemEdit);
        }
    }

}


