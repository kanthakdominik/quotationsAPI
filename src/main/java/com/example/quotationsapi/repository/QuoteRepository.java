package com.example.quotationsapi.repository;

import com.example.quotationsapi.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("SELECT q FROM Quote q JOIN Author a ON q.author.id = a.id WHERE a.name = :name AND a.surname = :surname")
    List<Quote> findAllByAuthors(@Param("name") String name, @Param("surname") String surname);
}
