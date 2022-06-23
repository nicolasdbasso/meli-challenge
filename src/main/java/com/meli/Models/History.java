package com.meli.Models;

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
@RedisHash("meli_history")
public class History {
	
	@Id
	@Indexed
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Indexed
	private String shortUrl;
	
	@Indexed
	private String urlrl;
	
}
