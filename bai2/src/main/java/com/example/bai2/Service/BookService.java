package com.example.bai2.Service;

import com.example.bai2.Model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private ArrayList<Book> books = new ArrayList<>(List.of(
            new Book(1L, "Clean Code", "Robert C. Martin"),
            new Book(2L, "Effective Java", "Joshua Bloch"),
            new Book(3L, "Spring in Action", "Craig Walls"),
            new Book(4L, "Head First Java", "Kathy Sierra")
    ));

    public List<Book> getAllBooks(){
        return books;
    }

    public Book getBookById(int id){
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void updateBook(int id, Book updatedBook){
        books.stream().filter(book -> book.getId() == id)
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                });
    }

    public void deleteBook(int id){
        books.removeIf(book -> book.getId() == id);
    }
}
