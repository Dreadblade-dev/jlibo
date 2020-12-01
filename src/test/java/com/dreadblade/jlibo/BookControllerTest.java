package com.dreadblade.jlibo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("anton")
@TestPropertySource("/application-test-dev.properties")
@Sql(value = { "/create-user-before.sql", "/books-list-before.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/books-list-after.sql", "/create-user-after.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/nav/div/div/a").string("anton"));
    }

    @Test
    @Transactional
    public void booksListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//div[@id='books-list']/div").nodeCount(4));
    }

    @Test
    @Transactional
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/").param("filter", "anna karenina"))
                .andDo(print())
                .andExpect(xpath("//div[@id='books-list']/div").nodeCount(1))
                .andExpect(xpath("//div[@id='books-list']/div[@data-id=7]").exists());
    }

    @Test
    @Transactional
    public void addBookToListTest() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "book.jpg",
                "image/jpeg", "SomeData...".getBytes());
        MockMultipartFile bookFile = new MockMultipartFile("bookFile", "book.pdf",
                "application/pdf", "SomeText...".getBytes());


        MockHttpServletRequestBuilder builder = multipart("/book/new")
                .file(imageFile)
                .file(bookFile)
                .param("author_id", "3")
                .param("title", "A title for book")
                .with(csrf())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                });

        this.mockMvc.perform(builder)
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/author/3"));
    }
}
