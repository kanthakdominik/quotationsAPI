package com.example.quotationsapi.service;

import com.example.quotationsapi.dto.AuthorDto;
import com.example.quotationsapi.dto.QuoteDto;
import com.example.quotationsapi.exception.ResourceNotFoundException;
import com.example.quotationsapi.model.Author;
import com.example.quotationsapi.model.Quote;
import com.example.quotationsapi.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public List<QuoteDto> getAllQuotes() {
        return quoteRepository.findAll().stream()
                .map(QuoteDto::new)
                .collect(Collectors.toList());
    }

    public QuoteDto getQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote with id = " + id + " not found"));
        return new QuoteDto(quote);
    }

    public Quote addQuote(QuoteDto quoteDto) {
        Quote newQuote = new Quote();
        Author newAuthor = new Author();

        newAuthor.setName(quoteDto.getAuthorDto().getName());
        newAuthor.setSurname(quoteDto.getAuthorDto().getSurname());
        newQuote.setAuthor(newAuthor);
        newQuote.setContent(quoteDto.getContent());

        return quoteRepository.save(newQuote);
    }

    public Quote updateQuote(Long id, QuoteDto quoteDto) {
        Quote updatedQuote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not updated! Quote with id = " + id + " not found."));
        Author updatedAuthor = updatedQuote.getAuthor();
        updatedAuthor.setName(quoteDto.getAuthorDto().getName());
        updatedAuthor.setSurname(quoteDto.getAuthorDto().getSurname());

        updatedQuote.setAuthor(updatedAuthor);
        updatedQuote.setContent(quoteDto.getContent());

        return quoteRepository.save(updatedQuote);
    }

    public void deleteQuote(Long id) {
        Quote deletedQuote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not deleted! Quote with id = " + id + " not found."));
        quoteRepository.delete(deletedQuote);
    }

    public void deleteAllQuotesByAuthor(AuthorDto authorDto) {
        List<Quote> deletedQuotes = quoteRepository.findAllByAuthors(authorDto.getName(), authorDto.getSurname());
        if (deletedQuotes.isEmpty()) {
            throw new ResourceNotFoundException("Not deleted! Quotes with author " + authorDto.getName() +
                    " " + authorDto.getSurname() + " not found.");
        }
        quoteRepository.deleteAll(deletedQuotes);
    }
}
