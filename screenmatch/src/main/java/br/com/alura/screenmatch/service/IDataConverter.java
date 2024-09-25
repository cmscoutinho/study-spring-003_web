package br.com.alura.screenmatch.service;

public interface IDataConverter {
    <T> T  obterDados(String json, Class<T> classe);
}
