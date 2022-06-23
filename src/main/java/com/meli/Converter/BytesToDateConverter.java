package com.meli.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;

@Component
@ReadingConverter
public class BytesToDateConverter implements Converter<byte[], Date> {

	@Override
	public Date convert(byte[] source) {

		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		String value = new String(source);
		try {
			return new Date(NumberUtils.parseNumber(value, Long.class));
		} catch (NumberFormatException nfe) {
			// ignore
		}
		try {
			return DateFormat.getInstance().parse(value);
		} catch (ParseException e) {
			// ignore
		}

		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			// ignore
		}

		throw new IllegalArgumentException(String.format("Cannot parse date out of %s", Arrays.toString(source)));
	}

}
