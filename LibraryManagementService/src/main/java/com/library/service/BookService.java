package com.library.service;

import com.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    Book save(MultipartFile imageBook, Book book);

    List<Book> getAllBookByCategoryID(Long cateID);

    String deleteBook(Long id);

    Book update(MultipartFile imageBook, Book book);

    List<Book> getAllBookByKeyword(String keyword);

    List<Book> getAllBookByCateIDAndKeyword(Long cateID, String keyword);

//    List<BookTopSellerDto> getTopSellerOfBook(int topNumber);


//    Client
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Page<Book> pageBooks(int pageNo);
    Page<Book> searchBookByKeyword(int pageNo, String keyword);

}
