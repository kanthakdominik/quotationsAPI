package com.example.quotationsapi.dto;

import com.example.quotationsapi.model.Quote;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class QuoteDto {

    public QuoteDto(Quote quote){
        this.authorDto = new AuthorDto(quote.getAuthor().getName(), quote.getAuthor().getSurname());
        this.content = quote.getContent();
    }

    @JsonProperty("author")
    @Valid
    private AuthorDto authorDto;

    @NotBlank
    private String content;
}
