package br.com.coutsoft.screenquote.repository;

import br.com.coutsoft.screenquote.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    public Quote getRandomQuote();
}
