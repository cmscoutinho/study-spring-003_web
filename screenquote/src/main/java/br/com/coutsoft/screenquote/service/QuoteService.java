package br.com.coutsoft.screenquote.service;

import br.com.coutsoft.screenquote.dto.QuoteDTO;
import br.com.coutsoft.screenquote.model.Quote;
import br.com.coutsoft.screenquote.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository repository;

    public QuoteDTO getRandomQuote() {
        Quote quote = repository.findById(4L).get();
        return new QuoteDTO(quote.getTitle(), quote.getQuote(), quote.getCharacter(), quote.getPoster());
    }
}
