package com.meli.Models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class Url {

    @JsonInclude(Include.NON_NULL)
	private String id;

	@NonNull
	private String url;

	@NonNull
	private String shortUrl;
	
    @JsonInclude(Include.NON_NULL)
	private Date created_date;

}
