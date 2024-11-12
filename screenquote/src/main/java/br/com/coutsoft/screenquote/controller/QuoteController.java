package br.com.coutsoft.screenquote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @GetMapping("/series/quotes")
    public QuoteDTO getRandomQuote() {
        return service.getRandomQuote();
    }
}
