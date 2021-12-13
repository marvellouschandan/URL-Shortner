package com.epam.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class URLMapperServiceImpl{
	private Map<String, String> dict = new HashMap<>();
	
	public Optional<String> getOriginalURL(String shortenedURL) {
		return Optional.ofNullable(dict.getOrDefault(shortenedURL, null));
	}
	
	public void setShortenedURL(String shortenedURL, String OriginalURL) {
		if (dict.containsKey(shortenedURL)) {
			dict.replace(shortenedURL, OriginalURL);
		}else {
			dict.put(shortenedURL, OriginalURL);
		}
	}
	
	public boolean isURLUnmapped(String shortenedURL) {
		return !(dict.containsKey(shortenedURL));
	}
}
