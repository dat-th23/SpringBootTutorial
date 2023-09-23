package com.library.service.impl;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final ImageUpload imageUpload;

    private String keyword;

    private Long cateID;

    /*        Admin       */

    @Override
    public Book save(MultipartFile imageBook, Book book) {
        try {
            Calendar cal = Calendar.getInstance();
            if (imageBook == null) {
                book.setImage(null);
            } else {
                if (imageUpload.uploadImage(imageBook)) {
                    System.out.println("Upload successfully");
                }
                book.setImage(Base64.getEncoder().encodeToString(imageBook.getBytes()));
            }
            book.setStatus(Book.BookStatus.UNAVAILABLE);

            // set time
            book.setCreatedAt(cal.getTime());
            book.setUpdatedAt(cal.getTime());
            return bookRepository.save(book);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Book update(MultipartFile imageBook, Book book) {
        try {
            Calendar cal = Calendar.getInstance();
            book.setUpdatedAt(cal.getTime());
            Book bookExisted = bookRepository.findById(book.getId()).get();

            // Check if imageBook is not null and not empty
            if (imageBook != null && !imageBook.isEmpty()) {
                if (imageUpload.checkExisted(imageBook) == false) {
                    imageUpload.uploadImage(imageBook);
                }
                bookExisted.setImage(Base64.getEncoder().encodeToString(imageBook.getBytes()));
            }
            bookExisted.setTitle(book.getTitle());
            bookExisted.setSubject(book.getSubject());
            bookExisted.setPublisher(book.getPublisher());
            bookExisted.setPublishedAt(book.getPublishedAt());
            bookExisted.setLanguage(book.getLanguage());
            bookExisted.setAuthor(book.getAuthor());
            bookExisted.setAmount(book.getAmount());
            bookExisted.setPrice(book.getPrice());
            bookExisted.setDescription(book.getDescription());
            bookExisted.setBorrowPrice(book.getBorrowPrice());
            bookExisted.setCategory(book.getCategory());
            bookExisted.setStatus(book.getStatus());

            // set time
            bookExisted.setCreatedAt(bookExisted.getCreatedAt());
            bookExisted.setUpdatedAt(book.getUpdatedAt());
            return bookRepository.save(bookExisted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /*        Client       */

    @Override
    public List<Book> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        return bookList;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.getById(id);
    }

    @Override
    public Page<Book> pageBooks(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        List<Book> books = bookRepository.findAll();
        Page<Book> bookPages = toPage(books, pageable);
        return bookPages;
    }

    @Override
    public Page<Book> searchBookByKeyword(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        List<Book> bookList = bookRepository.searchBookList(keyword);
        Page<Book> books = toPage(bookList, pageable);
        return books;
    }

    private Page toPage(List<Book> list, Pageable pageable) {

        // kiểm tra danh sách có trống không?
        if (pageable.getOffset() >= list.size()){
            return  Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex,endIndex);
        return new PageImpl(subList, pageable, list.size());
    }


    @Override
    public List<Book> getAllBookByCategoryID(Long cateID) {
        log.info("Dang tim tat ca books bang cateID ");
        return bookRepository.getAllBookByCategoryID(cateID);
    }

    @Override
    public List<Book> getAllBookByCateIDAndKeyword(Long cateID, String keyword) {
        this.cateID = cateID;
        this.keyword = keyword;
        List<Book> getByKeyword = bookRepository.getAllBooksByKeyword(keyword);
        List<Book> getByCateIDAndKeyword = new ArrayList<Book>();
        for (Book book : getByKeyword) {
            if (book.getCategory().getCategoryId() == cateID) {
                getByCateIDAndKeyword.add(book);
            }
        }
        return getByCateIDAndKeyword;
    }

    @Override
    public List<Book> getAllBookByKeyword(String keyword) {
        return bookRepository.getAllBooksByKeyword(keyword);
    }


    @Override
    public String deleteBook(Long id) {
        Book book = bookRepository.findById(id).get();
        if (book == null) {
            return "Cannot find Book " + id;
        } else {
            bookRepository.delete(book);
            return "Book " + id + " has been deleted !";
        }
    }

}
