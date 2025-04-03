package com.project;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@EnableWebMvc
@Configuration
@PropertySource(value= {"classpath:application.yaml"})
@ComponentScan(basePackages = "com.project")
public class ApplicationRunner extends AnnotationConfigWebApplicationContext {

    public static class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        @Override
        protected Class<?>[] getRootConfigClasses() {
            return null;
        }

        @Override
        protected Class<?>[] getServletConfigClasses() {
            return new Class[]{ApplicationRunner.class};
        }

        @Override
        protected String[] getServletMappings() {
            return new String[]{"/"};
        }

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            super.onStartup(servletContext);
        }

    }

    public static class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    }
}

