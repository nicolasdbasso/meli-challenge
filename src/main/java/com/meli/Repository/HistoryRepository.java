package com.meli.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.meli.Models.History;


@Repository
public interface HistoryRepository extends CrudRepository<History, Integer>{
	
	public List<History> findByUrlId(String urlId);
	
	public List<History> findByShortUrl(String shortUrl);
	
	public List<History> findByUrl (String url);

}
