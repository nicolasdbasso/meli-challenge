package com.meli.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenerException extends Exception{
	
	private String errorMessage;
	private String url;
	private Exception exception;

}
