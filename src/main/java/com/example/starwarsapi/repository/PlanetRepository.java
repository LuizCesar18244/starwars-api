package com.example.starwarsapi.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.starwarsapi.model.Planet;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, Integer>{
	
	public Optional<Planet> findById(Long id);
	public List<Planet> findAll();
	public List<Planet> findByName(String name);	
}
