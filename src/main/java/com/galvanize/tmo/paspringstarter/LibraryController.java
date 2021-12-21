package com.galvanize.tmo.paspringstarter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galvanize.tmo.data.Book;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LibraryController {

    private List<Book> library;

    public LibraryController(){
        library = new ArrayList<Book>();
    }

    @GetMapping("/health")
    public void health() {

    }

    @PostMapping("/api/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if(book.title == null) {
            System.out.println("==========================isNull=======================");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        library.add(book);
        return new ResponseEntity<Book>(book, HttpStatus.CREATED);
    }

    @GetMapping("/api/books")
    public ResponseEntity<Map<String,List<Book>>> getBooks() {
        Collections.sort(library);
        Map<String,List<Book>> map = new HashMap<>();
        map.put("books", library);
        return new ResponseEntity<Map<String,List<Book>>>(map,HttpStatus.OK);
    }

    @DeleteMapping("/api/books")
    public ResponseEntity<Object> burnBooks() {
        library = new ArrayList<Book>();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
