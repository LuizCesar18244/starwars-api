package com.example.starwarsapi.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet {

	@Id
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long id;
	private String name;
	private String climate;
	private String terrain;
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<String> films;
	@JsonProperty(access = Access.WRITE_ONLY)
	private int amountMovieAppearances;
	
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
	
	public void setName(String name) {
		this.name = name;
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
	
	public int getAmountMovieAppearances() {
		return this.amountMovieAppearances;
	}

	public void setAmountMovieAppearances( int amountMovieAppearances ) {
		this.amountMovieAppearances = amountMovieAppearances;
	}

}
