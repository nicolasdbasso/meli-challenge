package com.meli.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meli.DTO.UrlDTO;
import com.meli.Exception.ShortenerException;
import com.meli.Models.Log;
import com.meli.Repository.LogRepository;
import com.meli.Services.ShortenerService;

@RestController
@RequestMapping("/meli")
public class ShortenerController {

	@Autowired
	ShortenerService service;
	
	@Autowired
	LogRepository repositoryLog;
	
	private final String component = "ShortenerController";

	@GetMapping("/{url}")
	public ResponseEntity<String> goToURL(@PathVariable String url, HttpServletRequest request)
			throws URISyntaxException, IOException {
		
		String nameMethod = "goToUrl";

		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		
		try {

			return service.goToURL(url, request);

		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method("goToUrl").info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL " + e.getMessage() + " " + e.getLocalizedMessage(), null,
					HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/info")
	@ResponseBody
	public ResponseEntity<String> infoURL(@RequestParam String url) {
		
		String nameMethod = "infoUrl";
		
		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());


		try {

			return service.infoUrl(url);

		} catch (Exception e) {
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL " + e.getMessage() + " " + e.getLocalizedMessage(), null,
					HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping("/original")
	@ResponseBody
	public ResponseEntity<String> getOriginalURL(@RequestParam String shortUrl) {
		String nameMethod = "getOriginalUrl";
		repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());


		try {

			return service.getOriginalUrl(shortUrl);

		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL " + e.getMessage() + " " + e.getLocalizedMessage(), null,
					HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping("/all")
	@ResponseBody
	public Iterable<UrlDTO> getAll() {
		String nameMethod = "getAll";
		repositoryLog.save(Log.builder().component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());


			return service.getAll();


	}

	@PostMapping(value = "/generateUrl")
	public ResponseEntity<String> generateUrl(@RequestBody String url) {
		String nameMethod = "generateUrl";
		
		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());


		try {

			return service.generateUrl(url);

		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL " + e.getMessage() + " " + e.getLocalizedMessage(), null, HttpStatus.NOT_FOUND);
		}

	}

	@DeleteMapping(value = "/delete/{shortUrl}")
	public ResponseEntity<String> deleteUrl(@PathVariable String shortUrl) {
		String nameMethod = "deleteUrl";
		
		repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());


		try {
			return service.deleteUrl(shortUrl);

		} catch (ShortenerException e) {
			repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL " + e.getMessage() + " " + e.getLocalizedMessage(), null,
					HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			return new ResponseEntity<String>("ALGO SALIO MAL" + e.getMessage() + " " + e.getLocalizedMessage(), null,
					HttpStatus.NOT_FOUND);
		}

	}

}
