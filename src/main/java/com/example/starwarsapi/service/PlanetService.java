package com.example.starwarsapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.starwarsapi.model.Planet;
import com.example.starwarsapi.model.Planets;
import com.example.starwarsapi.repository.PlanetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlanetService {

	@Autowired
	private PlanetRepository planetRepository;

	public void savePlanet(Planet planet) {

		//Planet planetSaved = this.planetRepository.save(planet);
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

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.planetRepository.save(planetSaved);

	}

	private String getPlanetsFromApi() {
		
		final String uri = "https://swapi.co/api/planets/";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		String response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

		return response;
	}

}
