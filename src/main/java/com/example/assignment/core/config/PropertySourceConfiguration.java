package com.example.assignment.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:constraint-attribute-values.properties")
public class PropertySourceConfiguration {
}
