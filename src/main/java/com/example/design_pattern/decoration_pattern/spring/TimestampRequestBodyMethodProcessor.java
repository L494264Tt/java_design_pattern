package com.example.design_pattern.decoration_pattern.spring;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 21:10
 */

public class TimestampRequestBodyMethodProcessor implements HandlerMethodArgumentResolver {

    private RequestResponseBodyMethodProcessor processor;

    private ApplicationContext context;

    public TimestampRequestBodyMethodProcessor(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TimestampRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        setupProcessor();
        Object result = processor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (!(result instanceof Map)) {
            return result;
        }
        ((Map) result).put("Timestamp", System.currentTimeMillis());
        return result;
    }

    private void setupProcessor() {
        if (processor != null) {
            return;
        }
        RequestMappingHandlerAdapter adapter = this.context.getBean(RequestMappingHandlerAdapter.class);
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                this.processor = (RequestResponseBodyMethodProcessor) argumentResolver;
                return;

            }
        }
    }
}
