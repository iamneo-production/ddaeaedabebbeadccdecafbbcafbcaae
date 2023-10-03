package com.example;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null; // If you don't have a separate root config class
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MyAppConfig.class }; // Specify your configuration class
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Map to the root URL
    }
}