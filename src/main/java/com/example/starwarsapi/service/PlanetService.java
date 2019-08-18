package com.example.starwarsapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.starwarsapi.model.DatabaseSequence;
import com.example.starwarsapi.model.Planet;
import com.example.starwarsapi.model.Planets;
import com.example.starwarsapi.repository.PlanetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlanetService {

	@Autowired
	private PlanetRepository planetRepository;
	@Autowired
	private MongoOperations mongoOperations;

	public void savePlanet(Planet planet) {

		planet.setId(generateSequence(planet.getSequenceName()));
		setAmountMovies(planet);
	}

	public List<Planet> getAllPlanets() {

		return this.planetRepository.findAll();
	}

	public Optional<Planet> getById(Integer id) {
		return this.planetRepository.findById(id);
	}

	public List<Planet> getByName(String name) {
		return this.planetRepository.findByName(name);
	}

	public void deletePlanet(Integer id) {
		Optional<Planet> planet = this.planetRepository.findById(id);

		if (planet.isPresent())
			this.planetRepository.deleteById(id);

	}

	private void setAmountMovies(Planet planetSaved) {

		String response = getPlanetsFromApi();

		ObjectMapper objectMapper = new ObjectMapper();
		Planets planestFind = new Planets();
		try {
			planestFind = objectMapper.readValue(response, Planets.class);

			planestFind.getResults().forEach(planet -> {
				if (planet.getName().equalsIgnoreCase(planetSaved.getName())) {
					planetSaved.setAmountMovies(planet.getFilms().size());
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.planetRepository.save(planetSaved);

	}
	
	/**
	 * Metódo que busca os dados dos Planetas da API externa.<br>
	 * <b>OBS:</b> Foi necessário a criação do Header com o user-agent para contornar o erro de permissão (403 Forbidden).
	 * @return retona a resposta da requisição no formato de String.
	 */

	private String getPlanetsFromApi() {

		final String uri = "https://swapi.co/api/planets/";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		String response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

		return response;
	}

	/**
	 * Metódo que gera a sequencia dos Id's dos Planetas.
	 * @param sequenceName nome da sequencia a ser usada.
	 * @return o número de Id a ser utilizado no Planeta cadastrado.
	 * */
	private long generateSequence(String sequenceName) {

		Query query = new Query(Criteria.where("_id").is(sequenceName));
		DatabaseSequence counter = mongoOperations.findAndModify(query, new Update().inc("seq", 1),
				new FindAndModifyOptions().returnNew(true).upsert(true), DatabaseSequence.class);

		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
