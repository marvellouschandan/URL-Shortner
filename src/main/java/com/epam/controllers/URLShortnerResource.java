package com.epam.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.epam.models.URLRequestDTO;
import com.epam.services.URLMapperServiceImpl;
import com.google.common.hash.Hashing;

// https://stackoverflow.com/questions/17955777/redirect-to-an-external-url-from-controller-action-in-spring-mvc
@Controller
public class URLShortnerResource {
	@Autowired
	URLMapperServiceImpl urlMapperRepo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/")
	public ModelAndView welcome() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@PostMapping("/create")
	@ResponseBody
	public String create(@RequestBody URLRequestDTO urlRequestDTO) throws RuntimeException{
		String originalURL = urlRequestDTO.getUrl();
		String choosenShortenedURL = urlRequestDTO.getChoice();
		
		UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
		
		if (urlValidator.isValid(originalURL)) {
			String shortenedURL = null;
			
			if (Objects.nonNull(choosenShortenedURL)  && choosenShortenedURL.trim().length()>0) {
				if (urlMapperRepo.isURLUnmapped(choosenShortenedURL)) {
					shortenedURL = choosenShortenedURL.trim();
				}else {
					throw new RuntimeException("Shortened URL choice is invalid: " + choosenShortenedURL);
				}
			}else {
				shortenedURL = Hashing.murmur3_32().hashString(originalURL, StandardCharsets.UTF_8).toString();
			}
			
			logger.info("URL " + originalURL + " shortened to " + shortenedURL);
			urlMapperRepo.setShortenedURL(shortenedURL, originalURL);
			return shortenedURL;
		}
		
		throw new RuntimeException("URL invalid: " + originalURL);
	}
	
	@GetMapping("/link/{shortenedURL}")
	public void getURL(@PathVariable String shortenedURL, HttpServletResponse httpServletResponse) throws RuntimeException{
		Optional<String> originalURL = urlMapperRepo.getOriginalURL(shortenedURL);
		
		if (originalURL.isPresent()) {
			logger.info("Original url fetched: " + originalURL.get() + " for shortened URL : " + shortenedURL);
			httpServletResponse.setHeader("Location", originalURL.get());
		    httpServletResponse.setStatus(302);
		}else {
			throw new RuntimeException("Shortened URL invalid: " + shortenedURL);
		}
	}
}
