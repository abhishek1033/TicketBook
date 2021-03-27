package com.movie.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.movie.data.entity.Screening;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, Long> {
	
	// Provide Total screening by date
    List<Screening> findByScreeningDate(Date screeningDate);
    // find movie name
    List<Screening> findByMovieName(String movieName);
    //findByMovieNameAndTheatreIdAndScreeningDateAndScreeningTime
    // which movie in running in which theatre , screening date, screening time wise
    
    Screening findByMovieNameAndTheatreIdAndScreeningDateAndScreeningTime(String movieName, long theatreId, Date screeningDate, Time screeningTime);
}
