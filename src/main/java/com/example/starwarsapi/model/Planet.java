package com.example.starwarsapi.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet {

	@Id
	private Long id;
	@Transient
    private String SEQUENCE_PLANETS = "planets_sequence";
	private String name;
	private String climate;
	private String terrain;
	private List<String> films;
	private int amountMovies;
	
	public Planet() {}

	public Planet(String name, String climate, String terrain) {
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getClimate() {
		return this.climate;
	}

	public String getTerrain() {
		return this.terrain;
	}

	public List<String> getFilms() {
		return this.films;
	}
	
	public int getAmountMovies() {
		return this.amountMovies;
	}

	public void setAmountMovies(int amountMovies) {
		this.amountMovies = amountMovies;
	}

	public String getSequenceName() {
		return SEQUENCE_PLANETS;
	}
}
