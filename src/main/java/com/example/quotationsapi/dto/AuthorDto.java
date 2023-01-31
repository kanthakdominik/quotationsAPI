package com.example.quotationsapi.dto;

import com.example.quotationsapi.model.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthorDto {

    public AuthorDto(Author author) {
        this.name = author.getName();
        this.surname = author.getSurname();
    }

    @NotBlank
    private String name;

    @NotBlank
    private String surname;
}
