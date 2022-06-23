package com.meli.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@RedisHash("meli_url4")
public class UrlDTO implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@Id
	@Indexed
	@NonNull
	private String url;
	
	@NonNull
	@Id
	@Indexed
	private String shortUrl;
	
	@Temporal(TemporalType.DATE)
	private Date created_date;

}
