package com.nei.common.advice;

import com.nei.controller.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.Collections;

/**
 * 统一json返回处理
 */
@Slf4j
@ControllerAdvice(basePackages = "com.nei.controller")
public class ResultJsonResponseAdvice implements ResponseBodyAdvice {

	@Override
	public boolean supports(final MethodParameter returnType, final Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, final MethodParameter returnType, final MediaType selectedContentType,
                                  final Class selectedConverterType, final ServerHttpRequest request, final ServerHttpResponse response) {
		if (body == null) {
			final Class returnClass = (Class) returnType.getGenericParameterType();
			if (returnClass.isArray() || Collection.class.isAssignableFrom(returnClass)) {
				body = Collections.emptyList();
			} else if (String.class.equals(returnClass)) {
				body = "";
			} else {
				body = Collections.emptyMap();
			}
		}

		return ApiResponse.success(body);
	}
}
