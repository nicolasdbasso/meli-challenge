package com.meli.Converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class DateToBytesConverter implements Converter<Date, byte[]>{

	@Override
	public byte[] convert(Date source) {
		return source.toString().getBytes();
	}

}
