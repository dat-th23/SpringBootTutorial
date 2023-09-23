package com.library.controller;

import com.library.entity.Book;
import com.library.entity.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final CategoryRepository categoryRepository;

//    @GetMapping("/library/books/search")
//    public String getAllBooksByKeyword(@RequestParam("keyword") String keyword) {
//        return bookService.getAllBookByKeyword(keyword);
//    }


//    Book Admin Controller
    @GetMapping("/admin/books")
    public String book(Model model){
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("size", books.size());
        model.addAttribute("title", "Books");
        return "admin/book";
    }

    //    GetBookById(Book details admin)
    @GetMapping("/admin/books/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("title", "Book Detail");
        model.addAttribute("book", book);
        return "admin/book-detail";
    }



    //    SaveBook
    @GetMapping(value = "/admin/books/new")
    public String addForm(Model model) {
        Book book = new Book();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("title", "Add new book");
        model.addAttribute("book", book);
        return "admin/book-add";
    }

    @PostMapping("/admin/books/add")
    public String  createBook(@ModelAttribute("book") Book book,
                              @RequestParam("imageBook") MultipartFile imageBook,
                              BindingResult result) {
        if (result.hasErrors()){
            return "admin/book-add";
        }
        bookService.save(imageBook,book);
        return "redirect:/admin/books";
    }


    //    UpdateBookById
    @GetMapping("/admin/books/edit/{id}")
    public String updateForm(@PathVariable("id") long id, Model model){
        Book book = bookService.getBookById(id);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("title","Edit book");
        model.addAttribute("book", book);
        return "admin/book-edit";
    }

    @PostMapping("/admin/books/edit/{id}")
    public String updateBook(@PathVariable("id") long id,
                             @Valid @ModelAttribute("book") Book book,
                             @RequestParam("imageBook") MultipartFile imageBook,
                             BindingResult result){
        if(result.hasErrors()){
            book.setId(id);
            return "admin/book-edit";
        }
        bookService.update(imageBook,book);
    return "redirect:/admin/books";
    }

    //    RemoveBookById
    @GetMapping("/admin/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        bookRepository.delete(book);
        return "redirect:/admin/books";
    }

    //    Book Controller Client

    @GetMapping("/library/books")
    public String allBook(Model model) {
        List<Book> bookDtoList = bookService.getAllBooks();
        model.addAttribute("title", "Books");
        model.addAttribute("books",bookDtoList);
        model.addAttribute("size",bookDtoList.size());
        return "client/books/book-list";
    }

    @GetMapping("/library/books/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model) {
        Page<Book> books = bookService.pageBooks(pageNo);
        model.addAttribute("title","Books");
        model.addAttribute("size", books.getSize());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("books", books);
        return "client/books/book-list";
    }

    @GetMapping("/library/search-result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model){
        Page<Book> books = bookService.searchBookByKeyword(pageNo, keyword);
        model.addAttribute("title", "Search Result");
        model.addAttribute("books", books);
        model.addAttribute("keyword",keyword);
        model.addAttribute("size", books.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", books.getTotalPages());
        return "/client/books/result-books";
    }

    @GetMapping("/library/books/book-details/{id}")
    public String bookDetails(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
//        Long categoryId = book.getCategory().getCategoryId();
        model.addAttribute("title", "Single Book");
        model.addAttribute("book", book);
        return "client/books/book-single";
    }


    //    GetBookByCategory
    @GetMapping("/admin/books/category/{cateId}")
    public ResponseEntity<?> getBookByCateID(@PathVariable Long cateId) {
        if (bookService.getAllBookByCategoryID(cateId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List books is empty!");
        } else {
            return ResponseEntity.ok().body(bookService.getAllBookByCategoryID(cateId));
        }
    }

    //    SearchBookByCategoryIdOrKeyword
    @GetMapping("/admin/books/cateID-search")
    public ResponseEntity<?> getBookByCateIDAndKeyword(@RequestParam("cateID") Long cateID,
                                                       @RequestParam("keyword") String keyword) {
        if (bookService.getAllBookByCateIDAndKeyword(cateID, keyword) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List books is empty!");
        } else {
            return ResponseEntity.ok().body(bookService.getAllBookByCateIDAndKeyword(cateID, keyword));
        }
    }

}
