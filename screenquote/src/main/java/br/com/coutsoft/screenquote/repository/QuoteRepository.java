package br.com.coutsoft.screenquote.repository;

import br.com.coutsoft.screenquote.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("SELECT q FROM quote SORT BY function('Random') LIMIT 1")
    public Quote getRandomQuote();
}
