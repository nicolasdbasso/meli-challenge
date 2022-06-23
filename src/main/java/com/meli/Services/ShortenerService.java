package com.meli.Services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.meli.DTO.UrlDTO;
import com.meli.Exception.ShortenerException;
import com.meli.Models.EnumError;
import com.meli.Models.Error;
import com.meli.Models.History;
import com.meli.Models.Log;
import com.meli.Models.Url;
import com.meli.Repository.ErrorRepository;
import com.meli.Repository.HistoryRepository;
import com.meli.Repository.LogRepository;
import com.meli.Repository.UrlRepository;
import com.meli.Utils.CodeAndDecode;
import com.meli.Utils.ValidationURL;

@Service
public class ShortenerService {

	@Autowired
	private UrlRepository repositoryUrl;

	@Autowired
	private ErrorRepository repositoryError;

	@Autowired
	private HistoryRepository repositoryHistory;

	ObjectMapper mapper = new ObjectMapper();

	Gson gson = new Gson();
	
	@Autowired
	LogRepository repositoryLog;
	
	private final String component = "ShortenerController";

	public ResponseEntity<String> generateUrl(List<String> urlList) {
		
		String nameMethod = "generateUrl";

		repositoryLog.save(Log.builder().url("").component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		

		ArrayList<Error> errorList = new ArrayList<>();
		ArrayList<Url> urlSuccesfully = new ArrayList<>();

		for (String url : urlList) {

			try {

				if (!ValidationURL.isValidURL(url))
					throw new ShortenerException(EnumError.ERROR_M0004.name() + " ; " + url, url, null);

				UrlDTO shortUrl = generateAndSaveUrl(url);
				Url urlToView = Url.builder().url(shortUrl.getUrl()).shortUrl(shortUrl.getShortUrl()).build();
				urlSuccesfully.add(urlToView);

			} catch (ShortenerException exc) {
				repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());


				Error error = Error.builder().errorMessages(exc.getErrorMessage()).url(exc.getUrl())
						.exception(exc.getException().toString()).build();

				repositoryError.save(error);
				errorList.add(error);
				continue;

			} catch (Exception ex) {
				
				repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());


				Error error = Error.builder().errorMessages(EnumError.ERROR_M0000.name()).url(url)
						.exception(ex.toString()).build();

				repositoryError.save(error);
				errorList.add(error);
				continue;
			}

		}

		if (errorList.isEmpty())
			return new ResponseEntity<String>(gson.toJson(urlSuccesfully), null, HttpStatus.OK);

		return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);

	}

	private UrlDTO generateAndSaveUrl(String url) throws ShortenerException {
		
		String nameMethod = "generateAndSaveUrl";

		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		

		String shortUrl = null;

		try {

			UrlDTO urlRecorded = repositoryUrl.findByUrl(url);

			if (urlRecorded != null)
				throw new ShortenerException(
						EnumError.ERROR_M0002.name() + " ; La URL corta es: " + urlRecorded.getShortUrl(), url, null);

			if (!ValidationURL.isValidURL(url))
				throw new ShortenerException(EnumError.ERROR_M0004.name() + " ; " + url, url, null);

			shortUrl = shortener(url);

			return repositoryUrl.save(
					UrlDTO.builder().url(url).shortUrl(shortUrl).created_date(Date.valueOf(LocalDate.now())).build());
		} catch (DuplicateKeyException e) {
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());


			throw new ShortenerException(EnumError.ERROR_M0009.name(), url, e);
		} catch (DataIntegrityViolationException e) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());


			UrlDTO urlRecorded = repositoryUrl.findByUrl(url);
			ShortenerException ex = ShortenerException.builder().url(url).exception(e).build();

			ex.setErrorMessage(EnumError.ERROR_M0002 + " ; la url corta es: " + urlRecorded.getShortUrl());

			throw ex;

		} catch (IllegalArgumentException e) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			throw new ShortenerException(EnumError.ERROR_M0003.name(), url, e);
		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			if (e instanceof ShortenerException)
				throw e;

			throw new ShortenerException(EnumError.ERROR_M0001.name(), url, e);
		}

	}

	private String shortener(String url) {
		
		String nameMethod = "shortener";

		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		

		return CodeAndDecode.encode(url);
	}

	public ResponseEntity<String> deleteUrl(String shortUrl) throws ShortenerException {
		
		String nameMethod = "deleteUrl";

		repositoryLog.save(Log.builder().shortUrl(shortUrl).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		

		ArrayList<Error> errorList = new ArrayList<>();
		ArrayList<UrlDTO> urlSuccesfully = new ArrayList<>();

		String shortUrlComplete = getUrlShort(shortUrl);

		try {

			try {
				UrlDTO urlRecorded = repositoryUrl.findByShortUrl(shortUrlComplete);

				if (urlRecorded == null)
					throw new ShortenerException(
							EnumError.ERROR_M0004.name() + " Recuerde que solo debe ingresar la Key.", shortUrlComplete,
							null);

				repositoryUrl.delete(urlRecorded);

				urlSuccesfully.add(urlRecorded);

			} catch (IllegalArgumentException e) {
				
				repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

				Error error = Error.builder().errorMessages(EnumError.ERROR_M0003.name()).shortUrl(shortUrlComplete)
						.exception(e.toString()).build();

				repositoryError.save(error);
				errorList.add(error);

			} catch (Exception e) {
				
				repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

				Error error = Error.builder().errorMessages(EnumError.ERROR_M0001.name()).shortUrl(shortUrlComplete)
						.exception(e.toString()).build();

				if (e instanceof ShortenerException)
					error.setErrorMessages(EnumError.ERROR_M0004.name() + " Recuerde que solo debe ingresar la Key.");

				repositoryError.save(error);
				errorList.add(error);

			}

		} catch (Exception e) {
			// ignore
		}

		if (errorList.isEmpty())
			return new ResponseEntity<String>(gson.toJson(urlSuccesfully) + " Fue eliminada.", null, HttpStatus.OK);

		return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<String> infoUrl(String shortUrl) {
		
		String nameMethod = "infoUrl";
		
		String shortUrlComplete = getUrlShort(shortUrl);

		repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		

		ArrayList<Error> errorList = new ArrayList<>();

		try {
//			UrlDTO urlRecorded = repositoryUrl.findByUrl(url);

			UrlDTO urlRecorded = repositoryUrl.findByShortUrl(shortUrlComplete);

			if (urlRecorded == null)
				throw new ShortenerException(EnumError.ERROR_M0004.name(), shortUrlComplete, null);

			List<History> histories = repositoryHistory.findByShortUrl(shortUrlComplete);
			
			int redirect = 0;
			
			if (histories != null)
				redirect = histories.size();
			
			
			
			
			return new ResponseEntity<String>(urlRecorded.toString() + " La URL corta fue utilizada " + redirect +" veces.", null, HttpStatus.OK);

		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());


			Error error = Error.builder().errorMessages(EnumError.ERROR_M0004.name()).exception(e.toString()).shortUrl(shortUrlComplete).build();

			repositoryError.save(error);
			errorList.add(error);

			return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
		}

	}

	public ResponseEntity<String> generateUrl(String url) {
		
		String nameMethod = "generateUrl";

		repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		
		ArrayList<Error> errorList = new ArrayList<>();
		ArrayList<Url> urlSuccesfully = new ArrayList<>();

		try {

			UrlDTO shortUrl = generateAndSaveUrl(url);
			Url urlToView = Url.builder().url(shortUrl.getUrl()).shortUrl(shortUrl.getShortUrl()).build();
			urlSuccesfully.add(urlToView);

		} catch (ShortenerException exc) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().errorMessages(exc.getErrorMessage()).url(exc.getUrl())
					.exception(exc.getException() != null ? exc.getException().toString() : null).build();

			repositoryError.save(error);
			errorList.add(error);

		} catch (Exception ex) {
			
			repositoryLog.save(Log.builder().url(url).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().errorMessages(EnumError.ERROR_M0000.name()).url(url).exception(ex.toString())
					.build();

			repositoryError.save(error);
			errorList.add(error);
		}

		if (errorList.isEmpty())
			return new ResponseEntity<String>(gson.toJson(urlSuccesfully), null, HttpStatus.OK);

		return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<String> goToURL(String shortUrl, HttpServletRequest request) {
		
		String nameMethod = "goToUrl";
		String shortUrlComplete = getUrlShort(shortUrl);


		repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		
		ArrayList<Error> errorList = new ArrayList<>();
		URI uri;


		try {
			UrlDTO urlRecorded = repositoryUrl.findByShortUrl(shortUrlComplete);

			if (urlRecorded != null) {
				uri = new URI(urlRecorded.getUrl());
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(uri);

				repositoryHistory.save(History.builder().shortUrl(urlRecorded.getShortUrl()).urlrl(urlRecorded.getUrl()).build());

				return new ResponseEntity<String>(httpHeaders, HttpStatus.SEE_OTHER);
			} else {
				
				repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

				Error error = Error.builder().errorMessages(EnumError.ERROR_M0005.name()).shortUrl(shortUrlComplete)
						.build();

				repositoryError.save(error);
				errorList.add(error);

				return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);

			}
		} catch (URISyntaxException e) {
			repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().errorMessages(EnumError.ERROR_M0000.name()).shortUrl(shortUrlComplete)
					.exception(e.toString()).build();

			repositoryError.save(error);
			errorList.add(error);

			return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
		} catch (InvalidDataAccessResourceUsageException e) {
			repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().errorMessages(EnumError.ERROR_M0003.name()).shortUrl(shortUrlComplete)
					.exception(e.toString()).build();

			repositoryError.save(error);
			errorList.add(error);

			return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().errorMessages(EnumError.ERROR_M0000.name()).shortUrl(shortUrlComplete)
					.exception(ex.toString()).build();

			repositoryError.save(error);
			errorList.add(error);

			return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
		}
	}

	private String getUrlShort(String shortUrl) {
		
		return "http://localhost/" + shortUrl;

	}

	public Iterable<UrlDTO> getAll() {
		String nameMethod = "getAll";

		repositoryLog.save(Log.builder().component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		
		return repositoryUrl.findAll();

	}

	public ResponseEntity<String> getOriginalUrl(String shortUrl) {
		ArrayList<Error> errorList = new ArrayList<>();

		String shortUrlComplete = getUrlShort(shortUrl);

		String nameMethod = "getOriginalUrl";

		repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Ingresa al metodo").date(Date.valueOf(LocalDate.now())).build());
		
		try {
			UrlDTO urlRecorded = repositoryUrl.findByShortUrl(shortUrlComplete);

			if (urlRecorded == null)
				throw new ShortenerException(EnumError.ERROR_M0004.name(), shortUrlComplete, null);

			return new ResponseEntity<String>(urlRecorded.getUrl(), null, HttpStatus.OK);

		} catch (Exception e) {
			
			repositoryLog.save(Log.builder().shortUrl(shortUrlComplete).component(component).method(nameMethod).info("Finaliza con errores").date(Date.valueOf(LocalDate.now())).build());

			Error error = Error.builder().exception(e.toString()).shortUrl(shortUrlComplete).build();

			repositoryError.save(error);
			errorList.add(error);

			return new ResponseEntity<String>(gson.toJson(errorList), null, HttpStatus.NOT_FOUND);
		}
	}

}
