package com.library.spring_library_example.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.library.spring_library_example.models.Books;
import com.github.javafaker.Faker;
import com.library.spring_library_example.models.MyUser;
import com.library.spring_library_example.repository.BookRepository;
import com.library.spring_library_example.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {
    private BookRepository bookRepository;
    private UserRepository UseRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void LoadBookInDB(){
        if (bookRepository.count() == 0) {
            Faker faker = new Faker();
            List<Books> books = IntStream.rangeClosed(1, 100)
                    .mapToObj(i -> Books.builder()
                            .id(i)
                            .title(faker.book().title())
                            .author(faker.book().author())
                            .publisher(faker.book().publisher())
                            .idUser(0)
                            .build())
                    .toList();
            bookRepository.saveAll(books);
        }
    }

    public List<Books> allBooks() {
        return bookRepository.findAll();
    }

    public Books booksById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void addUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UseRepository.save(user);
    }

    @Transactional
    public String takeBook(int bookId, int userId) {
        Optional<Books> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Books book = bookOptional.get();
            if (book.getIdUser() == 0) {
                book.setIdUser(userId);
                bookRepository.save(book);
                return "Book taken successfully";
            } else {
                return "Book is already taken by another user";
            }
        } else {
            return "Book not found";
        }
    }

    @Transactional
    public String transferBook(int bookId, int fromUserId, int toUserId) {
        Optional<Books> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Books book = bookOptional.get();
            if (book.getIdUser() == fromUserId) {
                book.setIdUser(toUserId);
                bookRepository.save(book);
                return "Book transferred successfully";
            } else {
                return "Book does not belong to the user";
            }
        } else {
            return "Book not found";
        }
    }

    @Transactional
    public String exchangeBooks(int bookId1, int userId1, int bookId2, int userId2) {
        Optional<Books> bookOptional1 = bookRepository.findById(bookId1);
        Optional<Books> bookOptional2 = bookRepository.findById(bookId2);

        if (bookOptional1.isPresent() && bookOptional2.isPresent()) {
            Books book1 = bookOptional1.get();
            Books book2 = bookOptional2.get();

            if (book1.getIdUser() == userId1 && book2.getIdUser() == userId2) {
                book1.setIdUser(userId2);
                book2.setIdUser(userId1);
                bookRepository.save(book1);
                bookRepository.save(book2);
                return "Books exchanged successfully";
            } else {
                return "One or both of the books do not belong to the specified users";
            }
        } else {
            return "One or both of the books not found";
        }
    }
}
