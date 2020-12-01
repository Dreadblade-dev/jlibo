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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("anton")
@TestPropertySource("/application-test-dev.properties")
@Sql(value = { "/create-user-before.sql", "/books-list-before.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/books-list-after.sql", "/create-user-after.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @WithUserDetails("user")
    public void authorPageTest() throws Exception {
        this.mockMvc.perform(get("/author/3"))
                .andExpect(xpath("/html/body/div/div[1]/h1").string("\n" +
                        "            Leo Tolstoy\n" +
                        "        "))
                .andExpect(xpath("/html/body/div/div[1]/div/p").string("Author's description"))
                .andExpect(xpath("/html/body/div/div[1]/img").exists());
    }

    @Test
    @Transactional
    public void addAuthorTest() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "author.jpg",
                "image/jpeg", "SomeData...".getBytes());

        MockHttpServletRequestBuilder builder = multipart("/author/new")
                .file(imageFile)
                .param("name", "Alexander Pushkin")
                .param("description", "Alexander Sergeyevich Pushkin")
                .with(csrf())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                });

        this.mockMvc.perform(builder);
    }
}
