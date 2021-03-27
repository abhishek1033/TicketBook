package com.movie.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.movie.data.entity.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, String> {
	
	// This is giving details of movie to movie name
    Movie findByMovieName(String movieName);
    // This is provide details of id to movie name
    Movie findByMovieId(long movieId);
}
