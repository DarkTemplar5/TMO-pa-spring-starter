package com.galvanize.tmo.data;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Schema
public class Book implements Comparable {

    @JsonProperty("id")
    public int id;
    
    @JsonProperty("author")
    public String author;
    
    @JsonProperty("title")
    public String title;
    
    @JsonProperty("yearPublished")
    public int yearPublished;
    @Override
    public int compareTo(Object o) {
        Book other = (Book) o;
        return title.compareTo(other.title);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearPublished() {
        return yearPublished;
    }
    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }


}
