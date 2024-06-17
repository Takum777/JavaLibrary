package com.library.spring_library_example.controllers;

import java.util.List;
import com.library.spring_library_example.models.Books;
import com.library.spring_library_example.models.MyUser;
import com.library.spring_library_example.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
@AllArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring Library Example";
    }

    @GetMapping("/all-books")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Books> getAllBooks() {
        return service.allBooks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Books bookById(@PathVariable int id) {
        return service.booksById(id);
    }

    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser user) {
        service.addUser(user);
        return "User is saved";
    }

    @GetMapping("/take")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String takeBook(@RequestParam int bookId, @RequestParam int userId) {
        return service.takeBook(bookId, userId);
    }

    @GetMapping("/transfer")
    public String transferBook(@RequestParam int bookId, @RequestParam int fromUserId, @RequestParam int toUserId) {
        return service.transferBook(bookId, fromUserId, toUserId);
    }

    @GetMapping("/exchange")
    public String exchangeBooks(@RequestParam int bookId1, @RequestParam int userId1,
                                @RequestParam int bookId2, @RequestParam int userId2) {
        return service.exchangeBooks(bookId1, userId1, bookId2, userId2);
    }
}
