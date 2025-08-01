package com.youlai.boot.system.controller;

import com.example.library.entity.BookInfo;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书信息控制器，提供前端所需的RESTful接口
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 获取图书列表
     */
    @GetMapping
    public ResponseEntity<List<BookInfo>> getBookList(
            @RequestParam(required = false) String bookNo,
            @RequestParam(required = false) String bookName) {
        List<BookInfo> books = bookService.findBooks(bookNo, bookName);
        return ResponseEntity.ok(books);
    }

    /**
     * 根据ID获取图书详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookInfo> getBookById(@PathVariable Integer id) {
        BookInfo book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * 添加新图书
     */
    @PostMapping
    public ResponseEntity<BookInfo> addBook(@RequestBody BookInfo book) {
        BookInfo savedBook = bookService.save(book);
        return ResponseEntity.ok(savedBook);
    }

    /**
     * 更新图书信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookInfo> updateBook(
            @PathVariable Integer id,
            @RequestBody BookInfo book) {
        book.setId(id);
        BookInfo updatedBook = bookService.update(book);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
