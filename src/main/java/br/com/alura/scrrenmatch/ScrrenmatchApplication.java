package br.com.alura.scrrenmatch;

import br.com.alura.scrrenmatch.model.DadosSerie;
import br.com.alura.scrrenmatch.service.ConsumoApi;
import br.com.alura.scrrenmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrrenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScrrenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=teen+wolf&apikey=d343fc07");
		System.out.println(json);

		ConverterDados conversor = new ConverterDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println("**************************");
		System.out.println(dados);
		System.out.println("**************************");
	}
}
