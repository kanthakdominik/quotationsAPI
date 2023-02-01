package com.example.quotationsapi.controller;

import com.example.quotationsapi.dto.QuoteDto;
import com.example.quotationsapi.exception.ResourceNotFoundException;
import com.example.quotationsapi.model.Author;
import com.example.quotationsapi.model.Quote;
import com.example.quotationsapi.repository.QuoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QuoteControllerTest {

    @MockBean
    private QuoteRepository quoteRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<Quote> quotes = new ArrayList<>();
    private final List<QuoteDto> quotesDto = new ArrayList<>();

    @BeforeEach
    void initialize() {
        quotes.clear();
        quotesDto.clear();

        quotes.add(new Quote(1L, new Author(1L, "Albert", "Einstein"),
                "Two things are infinite: the universe and human stupidity, and I'm not sure about the universe."));
        quotes.add(new Quote(2L, new Author(2L, "Albert", "Einstein"),
                "If you can't explain it simply, you don't understand it well enough."));
        quotes.add(new Quote(3L, new Author(3L, "Mark", "Twain"),
                "If you tell the truth, you don't have to remember anything."));

        quotes.stream().map(QuoteDto::new).forEachOrdered(quotesDto::add);
    }

    @Test
    void shouldGetAllQuotes() throws Exception {
        when(quoteRepository.findAll()).thenReturn(quotes);


        MvcResult result = mockMvc.perform(get("/api/quotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(quotesDto.size()))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringCase(objectMapper.writeValueAsString(quotesDto));
    }

    @Test
    void shouldGetQuote() throws Exception {
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quotes.get(0)));

        MvcResult result = mockMvc.perform(get("/api/quotes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(quotes.get(0).getContent()))
                .andExpect(jsonPath("$.author.name").value(quotes.get(0).getAuthor().getName()))
                .andExpect(jsonPath("$.author.surname").value(quotes.get(0).getAuthor().getSurname()))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringCase(objectMapper.writeValueAsString(quotesDto.get(0)));
    }

    @Test
    void shouldNotGetQuoteByInvalidId() throws Exception {
        long invalidId = 0L;
        when(quoteRepository.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/quotes/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Quote with id = " + invalidId + " not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void shouldAddQuote() throws Exception {
        long newId = quotes.size() + 1;
        Quote newQuote = new Quote(newId, new Author(newId, "Benjamin", "Franklin"),
                "Tell me and I forget. Teach me and I remember. Involve me and I learn.");
        QuoteDto newQuoteDto = new QuoteDto(newQuote);
        when(quoteRepository.save(Mockito.any(Quote.class))).thenReturn(newQuote);

        MvcResult result = mockMvc.perform(post("/api/quotes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newQuoteDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringCase(objectMapper.writeValueAsString(newQuote));
    }

    @Test
    public void shouldUpdateQuote() throws Exception {
        Quote updatedQuote = new Quote(3L, new Author(3L, "Benjamin", "Franklin"),
                "Tell me and I forget. Teach me and I remember. Involve me and I learn.");

        when(quoteRepository.findById(3L)).thenReturn(Optional.of(quotes.get(2)));
        when(quoteRepository.save(Mockito.any(Quote.class))).thenReturn(updatedQuote);

        MvcResult result = mockMvc.perform(put("/api/quotes/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(updatedQuote.getContent()))
                .andExpect(jsonPath("$.author.name").value(updatedQuote.getAuthor().getName()))
                .andExpect(jsonPath("$.author.surname").value(updatedQuote.getAuthor().getSurname()))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualToIgnoringCase(objectMapper.writeValueAsString(updatedQuote));
    }

    @Test
    void shouldNotUpdateQuoteByInvalidId() throws Exception {
        long invalidId = 0L;
        Quote updatedQuote = new Quote(invalidId, new Author(invalidId, "Benjamin", "Franklin"),
                "Tell me and I forget. Teach me and I remember. Involve me and I learn.");
        when(quoteRepository.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/quotes/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuote)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Not updated! Quote with id = " + invalidId + " not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void shouldDeleteQuote() throws Exception {
        when(quoteRepository.findById(3L)).thenReturn(Optional.of(quotes.get(2)));
        doNothing().when(quoteRepository).deleteById(3L);

        MvcResult result = mockMvc.perform(delete("/api/quotes/{id}", 3L))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("Quote deleted");
    }

    @Test
    void shouldNotDeleteQuoteByInvalidId() throws Exception {
        long invalidId = 0L;
        when(quoteRepository.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/quotes/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Not deleted! Quote with id = " + invalidId + " not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void shouldDeleteQuoteByAuthor() throws Exception {
        List<Quote> einsteinQuotes = List.of(quotes.get(0), quotes.get(1));
        String name = "Albert";
        String surname = "Einstein";
        when(quoteRepository.findAllByAuthors(name, surname)).thenReturn(einsteinQuotes);
        doNothing().when(quoteRepository).deleteAll();

        MvcResult result = mockMvc.perform(delete("/api/quotes/author")
                        .param("name", name)
                        .param("surname", surname))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("Quotes for author: "
                + name + " " + surname + " deleted");
    }

    @Test
    void shouldNotDeleteQuoteByInvalidAuthor() throws Exception {
        String name = "None";
        String surname = "Null";
        when(quoteRepository.findAllByAuthors(name, surname)).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/api/quotes/author")
                        .param("name", name)
                        .param("surname", surname))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Not deleted! Quotes with author: " + name + " " + surname
                        + " not found.", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}
