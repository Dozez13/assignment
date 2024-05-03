package com.example.assignment.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.example.assignment.domain.repository")
@EntityScan("com.example.assignment.domain.entity")
public class JpaConfig {
}
