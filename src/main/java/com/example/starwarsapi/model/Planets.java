package com.example.starwarsapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Planets {

	private List<Planet> results;

	public List<Planet> getResults() {
		return results;
	}

	public void setResults(List<Planet> results) {
		this.results = results;
	}

}
