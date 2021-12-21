package com.galvanize.tmo.paspringstarter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.contains;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.galvanize.tmo.data.Book;

@SpringBootTest
@AutoConfigureMockMvc
class PaSpringStarterApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void isHealthy() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk());
	}

    @BeforeEach
    void before() throws Exception {
        mockMvc.perform(delete("/api/books"));
    }

    
	@Test
	void addBooks() throws Exception {
        Book add = new Book();
        String title = "The Hitchhiker's Guide to the Galaxy";
        String author = "Douglas Adams";
        int yearPublished = 1979;
        add.setTitle(title);
        add.setAuthor(author);
        add.setYearPublished(yearPublished);
		mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(add)))
		        .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.author", is(author)))
                .andExpect(jsonPath("$.yearPublished", is(yearPublished)));
	}

	@Test
	void getBooks() throws Exception {
        String [] authors = {"Douglas Adams", "Philip K. Dick", "William Gibson"};
        String [] titles = {
                    "The Hitchhiker's Guide to the Galaxy",
                    "Do Androids Dream of Electric Sheep?",
                    "Neuromancer"
                };
        int [] ids = {1,2,3};
        int [] published = {1979, 1968, 1984};
        Book add = new Book();

        for(int i=0;i<authors.length;i++) {
            add.setTitle(titles[i]);
            add.setAuthor(authors[i]);
            add.setYearPublished(published[i]);
            add.setId(ids[i]);
            mockMvc.perform(post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getJson(add)));
        }

		mockMvc.perform(get("/api/books"))
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(3)))
                .andExpect(jsonPath("$.books[*].title", contains("Do Androids Dream of Electric Sheep?",
                                                                 "Neuromancer",
                                                                 "The Hitchhiker's Guide to the Galaxy")));
	}

	@Test
	void burnBooks() throws Exception {
        mockMvc.perform(delete("/api/books"));

		mockMvc.perform(get("/api/books"))
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(0)));
	}

    private byte [] getJson(Object o) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            mapper.writeValue(outputStream, o);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
