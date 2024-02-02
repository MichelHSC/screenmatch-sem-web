package br.com.alura.scrrenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class <T> classe);


}
