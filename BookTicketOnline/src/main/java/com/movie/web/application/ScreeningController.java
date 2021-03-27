package com.movie.web.application;

import com.move.business.service.ScreeningService;
import com.movie.business.domain.MovieScreening;
import com.movie.data.entity.Movie;
import com.movie.data.entity.Screening;
import com.movie.data.entity.Ticket;
import com.movie.data.repository.MovieRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/screenings")
public class ScreeningController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningService screeningService;
    
    
	private static final DateFormat DATE_FORMAT_WITH_DASH = new SimpleDateFormat("dd-MM-yyyy");
    
    
    
	
    @RequestMapping(method = RequestMethod.GET)
    public String getScreenings(@RequestParam(value = "movie", required = true)String movieString, Model model) {
        List<MovieScreening> result = this.screeningService.getMovieScreeningsByMovie(movieString);
        model.addAttribute("screenings", result);
        model.addAttribute("movie", movieRepository.findByMovieName(movieString));
        return "screenings";
    }

////////////////////////////////
/// This Controller is used for when user is show the movie details and then
// is showing a screen like available sheets in screen 
// then is booked any sheet.
// so there required to display available sheet count and total sheet count
////////////////////////////////////////////// 
    
    @RequestMapping(method = RequestMethod.POST)
    public String bookSeats(@Valid @ModelAttribute MovieScreening movieBooking) {

    	Date today=new Date();
    	
    	// save only current date screening details
    	String toDayDate=DATE_FORMAT_WITH_DASH.format(today);
    	if(toDayDate.equalsIgnoreCase(movieBooking.getScreeningDate()))
    	{	
	        int bookedSeats = this.screeningService.getBookedSeats(movieBooking);
	        int totalSeats = this.screeningService.getTotalSeats(movieBooking);
	        Date currentDate=new Date();
	        
	        if ((bookedSeats+movieBooking.getNumSeats()) > totalSeats)
	            return "error";
	
	       
	        this.screeningService.bookSeats(movieBooking, bookedSeats+movieBooking.getNumSeats());
    	}
        return "result";
    }
}
