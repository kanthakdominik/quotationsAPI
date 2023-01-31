package com.example.quotationsapi.controller;

import com.example.quotationsapi.dto.AuthorDto;
import com.example.quotationsapi.dto.QuoteDto;
import com.example.quotationsapi.model.Quote;
import com.example.quotationsapi.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/quotes")
public class QuoteController {

    QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public ResponseEntity<List<QuoteDto>> getAllQuotes() {
        List<QuoteDto> quotes = quoteService.getAllQuotes();
        return new ResponseEntity<>(quotes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDto> getQuote(@PathVariable(name = "id") Long id) {
        QuoteDto quote = quoteService.getQuoteById(id);
        return new ResponseEntity<>(quote, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Quote> addQuote(@RequestBody @Valid QuoteDto quoteDto) {
        Quote newQuote = quoteService.addQuote(quoteDto);
        return new ResponseEntity<>(newQuote, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable(name = "id") Long id, @RequestBody @Valid QuoteDto quoteDto) {
        Quote updatedQuote = quoteService.updateQuote(id, quoteDto);
        return new ResponseEntity<>(updatedQuote, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuote(@PathVariable(name = "id") Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.ok("Quote deleted");
    }

    @PostMapping("deleteByAuthor")
    public ResponseEntity<?> deleteAllQuotesByAuthor(@RequestBody @Valid AuthorDto authorDto) {
        quoteService.deleteAllQuotesByAuthor(authorDto);
        return ResponseEntity.ok("Quotes for author " + authorDto.getName() + " " + authorDto.getSurname() + " deleted");
    }
}