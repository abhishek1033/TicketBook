package com.move.business.service;

import com.movie.business.domain.MovieScreening;
import com.movie.data.entity.*;
import com.movie.data.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScreeningService {
	
	
// This Service Provide details of movie date wise screening
//	1) getScreening method is provice Theater name and theater city wise screening details.
// 2) Here facility to Book Ticket - bookSeats	
// 3) getTotalBook Sheet
// 4)	getBookedSeats
//	5) getMoviesByDate
//	6) getMovieScreeningsByMovie
//	7)
	
    private ScreeningRepository screeningRepository;
    private MovieRepository movieRepository;
    private TheatreRepository theatreRepository;
    private TicketRepository ticketRepository;
    private ScreenRepository screenRepository;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, TheatreRepository theatreRepository
                            , TicketRepository ticketRepository, ScreenRepository screenRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.ticketRepository = ticketRepository;
        this.screenRepository = screenRepository;
    }
//total screening in date and time wise
    private Screening getScreening(MovieScreening movieScreening) {
        Theatre theatre = theatreRepository.findByTheatreNameAndTheatreCity(movieScreening.getTheatreName(), movieScreening.getTheatreCity());
        if (theatre == null)
            return null;
        return screeningRepository.findByMovieNameAndTheatreIdAndScreeningDateAndScreeningTime(movieScreening.getMovieName(), theatre.getTheatreId(),
                java.sql.Date.valueOf(movieScreening.getScreeningDate()), java.sql.Time.valueOf(movieScreening.getScreeningTime()));
    }

 // pre ticket booking facility   
    public int bookSeats(MovieScreening movieScreening, int seats) {
        Screening screening = getScreening(movieScreening);
        screening.setBookedTickets(seats);
        screeningRepository.save(screening);
        return getBookedSeats(movieScreening);
    }
// This is provide details of booked sheet details for user and admin
    public int getBookedSeats(MovieScreening movieScreening) {
        Screening screening = getScreening(movieScreening);
        return screening.getBookedTickets();
    }
// This is provide details of total seat sell in movie
    public int getTotalSeats(MovieScreening movieScreening) {
        Screening screening = getScreening(movieScreening);
        long screenId = screening.getScreenId();
        return screenRepository.findByScreenId(screenId).getSeatsNum();
    }

    // get Movie details from searching date in srceening // This is completion after movie time details
    public Set<Movie> getMoviesByDate(Date date) {
        Iterable<Screening> screenings = this.screeningRepository.findByScreeningDate(new java.sql.Date(date.getTime()));
        Set<Movie> movies = new HashSet<>();

        if (screenings != null) {
            for (Screening screening : screenings) {
                Movie movie = movieRepository.findByMovieName(screening.getMovieName());
                movies.add(movie);
            }
        }

        return movies;
    }

    //Search The Movie name and get only movie by details
    public List<Screening> getScreeningsByMovie(String movieName) {
        return this.screeningRepository.findByMovieName(movieName);
    }

    
    // This Method Provide details of which movie in how many screening completed.
    
    // screening table provide screening entry
    //MovieScreening table provide details of movie deatails by screen wise
    public List<MovieScreening> getMovieScreeningsByMovie(String movieName) {
        Iterable<Screening> screenings = this.screeningRepository.findByMovieName(movieName);
        List<MovieScreening> movieScreenings = new ArrayList<>();

        if (screenings != null) {
            for (Screening screening : screenings) {
                MovieScreening movieScreening = new MovieScreening();
                Theatre theatre = theatreRepository.findByTheatreId(screening.getTheatreId());
                Movie movie = movieRepository.findByMovieName(screening.getMovieName());
                movieScreening.setMovieName(screening.getMovieName());
                movieScreening.setMoviePosterURL(movie.getMoviePosterUrl());
                if (theatre != null) {
                    movieScreening.setTheatreId(theatre.getTheatreId());
                    movieScreening.setTheatreName(theatre.getTheatreName());
                    movieScreening.setTheatreCity(theatre.getTheatreCity());
                }
                movieScreening.setScreeningDate(screening.getScreeningDate().toString());
                movieScreening.setScreeningTime(screening.getScreeningTime().toString());

                movieScreenings.add(movieScreening);
            }
        }

        return movieScreenings;
    }
}
