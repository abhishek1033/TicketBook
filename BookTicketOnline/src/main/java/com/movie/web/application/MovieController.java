package com.movie.web.application;

import com.move.business.service.ScreeningService;
import com.movie.business.domain.MovieScreening;
import com.movie.data.entity.Movie;
import com.movie.data.entity.Screening;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/movies")
public class MovieController {
	
	/**
	 * movie Controller
	 
	 This Controller use for getting ticket by selected date
	 
	 
	 * 
	 * 
	 */
	
	
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScreeningService screeningService;

    @RequestMapping(method = RequestMethod.GET)
    public String getMovies(@RequestParam(value = "date", required = false)String dateString, Model model) {
        Date date = null;
        if (dateString != null) {
            try 
            {
                date = DATE_FORMAT.parse(dateString);
            } catch (Exception pe) 
            {
                date = new Date();
            }
        } else {
            date = new Date();
        }
        Set<Movie> result = this.screeningService.getMoviesByDate(date);
        model.addAttribute("movies", result);
        model.addAttribute("movieBooking", new MovieScreening());
        return "movies";
    }
   
    
}
