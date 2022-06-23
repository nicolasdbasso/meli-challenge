package com.meli.Models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("meli_error")
public class Error implements Serializable{
	
	@Id
	@Indexed
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Indexed
	private String url;
	
	@Indexed
	private String shortUrl;

	@NonNull
	private String errorMessages;
	
	private String exception;
	
}
