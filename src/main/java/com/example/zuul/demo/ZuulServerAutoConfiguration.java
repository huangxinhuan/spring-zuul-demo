package com.example.zuul.demo;


import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.http.ZuulServlet;
import com.netflix.zuul.monitoring.CounterFactory;
import com.netflix.zuul.monitoring.TracerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ZuulServerAutoConfiguration {

    @Bean
    public ZuulController zuulController() {
        return new ZuulController();
    }

    @Bean
    public ZuulHandlerMapping zuulHandlerMapping(ZuulController zc){
        return new ZuulHandlerMapping(zc);
    }

    @Bean
    public ServletRegistrationBean zuulServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new ZuulServlet(), "/zuul/*");
        // The whole point of exposing this servlet is to provide a route that doesn't
        // buffer requests.
        servlet.addInitParameter("buffer-requests", "false");
        return servlet;
    }

    @Bean
    public ServletRegistrationBean zuulAsyncServlet() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200,
                50000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));

        ServletRegistrationBean servlet = new ServletRegistrationBean(new ZuulAsyncServlet(executor), "/async/*");
        servlet.setAsyncSupported(true);
        servlet.addInitParameter("buffer-requests", "false");
        return servlet;
    }

    @Bean
    public DebugFilter debugFilter() {
        return new DebugFilter();
    }

    @Configuration
    protected static class ZuulFilterConfiguration {

        @Autowired
        private Map<String, ZuulFilter> filters;

        @Bean
        public ZuulFilterInitializer zuulFilterInitializer(
                CounterFactory counterFactory, TracerFactory tracerFactory) {
            FilterLoader filterLoader = FilterLoader.getInstance();
            FilterRegistry filterRegistry = FilterRegistry.instance();
            return new ZuulFilterInitializer(this.filters, counterFactory, tracerFactory, filterLoader, filterRegistry);
        }

    }

    @Configuration
    protected static class ZuulMetricsConfiguration {

        @Bean
        @ConditionalOnMissingBean(CounterFactory.class)
        public CounterFactory counterFactory() {
            return new EmptyCounterFactory();
        }

        @ConditionalOnMissingBean(TracerFactory.class)
        @Bean
        public TracerFactory tracerFactory() {
            return new EmptyTracerFactory();
        }

    }


}
