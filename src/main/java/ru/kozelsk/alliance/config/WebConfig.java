package ru.kozelsk.alliance.config;

import org.hibernate.query.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/advertisementSale/**")
                .addResourceLocations("file:/M:/uploads/advertisementSale/");
        registry.addResourceHandler("/images/advertisementRent/**")
                .addResourceLocations("file:/M:/uploads/advertisementRent/");
        registry.addResourceHandler("/images/tour/**")
                .addResourceLocations("file:/M:/uploads/tour/");
        registry.addResourceHandler("/images/coursework/**")
                .addResourceLocations("file:/M:/uploads/coursework/");
        registry.addResourceHandler("/images/insurance/**")
                .addResourceLocations("file:/M:/uploads/insurance/");
    }
}
