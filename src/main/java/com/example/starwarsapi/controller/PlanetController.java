package com.example.starwarsapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.starwarsapi.model.Planet;
import com.example.starwarsapi.model.Response;
import com.example.starwarsapi.service.PlanetService;

@RestController
@RequestMapping("/api")
public class PlanetController
{
	@Autowired
	private PlanetService planetService;
	
	@RequestMapping( value="/planet", method = RequestMethod.POST )
	public  @ResponseBody Response save( @RequestBody Planet planet )
	{
		 return this.planetService.savePlanet(planet);			
	}
	
	@RequestMapping( value="/planet/{id}", method = RequestMethod.GET )
	public Optional<Planet> getPlanetById( @PathVariable( "id" ) Integer id )
	{
		return this.planetService.getById( id );			
	}
	
	@RequestMapping( value="/planet", method = RequestMethod.GET )
	public List<Planet> getPlanetByName( @RequestParam("name") String name )
	{
		return this.planetService.getByName( name.trim( ) );			
	}
	
	@RequestMapping( value="/planets", method = RequestMethod.GET )
	public List<Planet> getAllplanets( )
	{
		return this.planetService.getAllPlanets( );			
	}
	
	@RequestMapping( value="/planet/{id}", method = RequestMethod.DELETE )
	public Response deletePlanet( @PathVariable("id") Integer id )
	{
		 return this.planetService.deletePlanet( id );			
	}

}
