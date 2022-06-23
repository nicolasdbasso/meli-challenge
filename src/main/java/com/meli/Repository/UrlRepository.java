package com.meli.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.meli.DTO.UrlDTO;

@Repository
public interface UrlRepository extends CrudRepository<UrlDTO, Integer>{
	
	UrlDTO findByUrl(String url);
	
	UrlDTO findByShortUrl(String shortUrl);
	

}
