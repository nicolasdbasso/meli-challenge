package com.meli.Models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@RedisHash("meli_log")
public class Log {
	
	@Id
	@Indexed
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@NonNull
	@Indexed
	private String component;
	
	@NonNull
	@Indexed
	private String method;
	
	@NonNull
	@Indexed
	private String info;
	
	@Indexed
	private String url;
	
	@Indexed
	private String shortUrl;
	
	@Indexed
	@Temporal(TemporalType.DATE)
	private Date date;


}
