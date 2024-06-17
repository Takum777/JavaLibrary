package com.library.spring_library_example.repository;

import com.library.spring_library_example.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Books, Integer> {
}
