package com.meli.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.meli.Models.Log;

@Repository
public interface LogRepository extends CrudRepository<Log, Integer>{
	
	public List<Log> findByDate(Date date);
	
	public List<Log> findByUrl (String url);
	
	public List<Log> findByShortUrl (String shortUrl);
	
	public List<Log> findByMethod (String nameMethod);

}
