package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.meli.DTO.UrlDTO;

@SpringBootApplication()
@ComponentScan ({"com.*"})
@EnableRedisRepositories(basePackages = { "com.meli.Repository.*" } )
@EntityScan("com.meli.*")
public class ShortenerAppApplication {

	
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory () {
		return new JedisConnectionFactory();
	}
	
	@Bean
	RedisTemplate<String, UrlDTO> redisTemplate(){
		RedisTemplate<String, UrlDTO> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ShortenerAppApplication.class, args);
	}

}