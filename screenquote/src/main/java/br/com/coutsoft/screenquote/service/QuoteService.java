package br.com.coutsoft.screenquote.service;

import br.com.coutsoft.screenquote.dto.QuoteDTO;
import br.com.coutsoft.screenquote.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class QuoteService {

    @Autowired
    private QuoteRepository repository;

    public QuoteDTO getRandomQuote() {
        return repository.getRandomQuote();
    }
}
