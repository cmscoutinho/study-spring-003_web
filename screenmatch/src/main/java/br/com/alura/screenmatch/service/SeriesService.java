package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesService {


    @Autowired
    private SeriesRepository repository;

}
