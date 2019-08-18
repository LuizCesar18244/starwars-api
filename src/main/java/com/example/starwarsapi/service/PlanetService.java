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
import com.example.starwarsapi.model.Response;
import com.example.starwarsapi.repository.PlanetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlanetService
{
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	private final static String planetSequence = "planets_sequence";
	
	/**
	 * Salva um novo Planeta.
	 * @param planet
	 * @return Resposta da requisição.
	 */
	public Response savePlanet( Planet planet )
	{
		try {
			planet.setName( planet.getName( ).trim( ) );
			planet.setId( generateSequence( planetSequence ) );
			setAmountMovies( planet );

			return new Response( 1, "Planeta salvo com sucesso!" );

		} catch ( Exception e ) 
		{
			return new Response( 0, e.getMessage( ) );
		}
	}
	
	/**
	 * Busca todos os Planetas.
	 * 
	 * @return Uma Lista com todos os Planetas.
	 */
	public List<Planet> getAllPlanets( ) 
	{
		return this.planetRepository.findAll( );
	}

	/**
	 * Busca um Planeta pelo Id.  
	 * 
	 * @return O Planeta relacionado ao Id infomado.
	 */
	public Optional<Planet> getById( Integer id )
	{
		return this.planetRepository.findById( id );
	}

	/**
	 * Busca um Planeta pelo Nome.  
	 * 
	 * @return Os Planetas relacionados ao Nome informado.
	 */
	public List<Planet> getByName( String name )
	{
		return this.planetRepository.findByName( name );
	}
	
	/**
	 * Deleta um Planeta.
	 * @param id o Id do Planeta a ser deletado.
	 * @return Resposta da requisição.
	 */
	public Response deletePlanet( Integer id )
	{
		try {
			Optional<Planet> planet = this.planetRepository.findById( id );
			
			if ( planet.isPresent( ) ) 
			{
				this.planetRepository.deleteById( id );
				return new Response( 1, "Planeta deletado com sucesso!" );
			}
			
			return new Response( 1, "Planeta não localizado!" );
			
		} catch ( Exception e ) 
		{
			return new Response( 0, e.getMessage( ) );
		}
	}
	
	/**
	 * Adiciona a quantidade de aparições em filmes ao Planeta a ser salvo.
	 * @param planetSaved Planeta que será salvo.
	 * @return Resposta da requisição.
	 */
	private void setAmountMovies( Planet planetSaved ) 
	{
		String response = getPlanetsFromExternalApi( );

		ObjectMapper objectMapper = new ObjectMapper( );
		Planets planestFind = new Planets( );
		try {
			planestFind = objectMapper.readValue( response, Planets.class );

			planestFind.getResults( ).forEach(planet -> 
			{
				if ( planet.getName( ).trim( ).equalsIgnoreCase( planetSaved.getName( ) ) ) 
				{
					planetSaved.setAmountMovieAppearances( planet.getFilms( ).size( ) );
				}
			});

		} catch ( IOException e ) 
		{
			e.printStackTrace( );
		}
		this.planetRepository.save( planetSaved );
	}

	/**
	 * Metódo que busca os dados dos Planetas da API externa.<br>
	 * <b>OBS:</b> Foi necessário a criação do Header com o user-agent para
	 * contornar o erro de permissão (403 Forbidden).
	 * 
	 * @return retona a resposta da requisição no formato de String.
	 */
	private String getPlanetsFromExternalApi( ) 
	{
		//Uri da API externa
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
	 * Gera a sequencia dos Id's dos Planetas.
	 * 
	 * @param sequenceName nome da sequencia a ser usada.
	 * @return o número de Id a ser utilizado no Planeta cadastrado.
	 */
	private long generateSequence(String sequenceName)
	{

		Query query = new Query(Criteria.where("_id").is(sequenceName));
		DatabaseSequence counter = mongoOperations.findAndModify(query, new Update().inc("seq", 1),
				new FindAndModifyOptions().returnNew(true).upsert(true), DatabaseSequence.class);

		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
