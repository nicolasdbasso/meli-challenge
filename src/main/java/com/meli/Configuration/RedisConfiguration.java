package com.meli.Configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

import com.meli.Converter.BytesToDateConverter;
import com.meli.Converter.DateToBytesConverter;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(Arrays.asList(new DateToBytesConverter(), new BytesToDateConverter()));
    }
}
