package com.example.bai3.service;

import com.example.bai3.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private ArrayList<Book> books = new ArrayList<>(List.of(
            new Book(1L, "Clean Code", "Robert C. Martin"),
            new Book(2L, "Effective Java", "Joshua Bloch"),
            new Book(3L, "Spring in Action", "Craig Walls"),
            new Book(4L, "Head First Java", "Kathy Sierra")
    ));
    private Long nextId = 5L;

    public List<Book> getAllBooks(){
        return books;
    }

    public Optional<Book> getBookById(Long id){
        return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    public void addBook(Book book){
        book.setId(nextId++);
        books.add(book);
    }

    public void updateBook(Book updatedBook){
        books.stream().filter(book -> book.getId().equals(updatedBook.getId()))
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                });
    }

    public void deleteBook(Long id){
        books.removeIf(book -> book.getId().equals(id));
    }
}
