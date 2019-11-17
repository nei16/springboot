package com.nei.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JsonUtil {

	private static ObjectMapper getInstance() {
		return JacksonHolder.INSTANCE;
	}

	public static <O> String json(final O o) {
		if (o == null) {
			return null;
		}
		try {
			return getInstance().writeValueAsString(o);
		} catch (final JsonProcessingException e) {
			throw new IllegalArgumentException("json序列化失败", e);
		}
	}

	public static <T> T parse(final String json, final Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return getInstance().readValue(json, clazz);
		} catch (final IOException e) {
			throw new IllegalArgumentException("json反列化失败", e);
		}
	}

	public static <T> T parse(final String json, final JavaType valueType) {
		if (json == null) {
			return null;
		}
		try {
			return getInstance().readValue(json, valueType);
		} catch (final IOException e) {
			throw new IllegalArgumentException("json反列化失败", e);
		}
	}

	public static <T> T parse(final String json, final TypeReference valueTypeRef) {
		if (json == null) {
			return null;
		}
		try {
			return getInstance().readValue(json, valueTypeRef);
		} catch (final IOException e) {
			throw new IllegalArgumentException("json反列化失败", e);
		}
	}

	public static JavaType constructParametricType(final Class<?> parametrized, final Class... parameterClasses) {
		return getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	private static class JacksonHolder {
		private static final ObjectMapper INSTANCE = new ObjectMapper();

		static {
			INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			INSTANCE.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		}
	}
}
