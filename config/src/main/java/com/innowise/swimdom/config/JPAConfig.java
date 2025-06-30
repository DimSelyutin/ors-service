package com.innowise.swimdom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration JPA Auditing.
 * Include auto filling fields createdBy, modifiedBy and other.
 */
@EnableJpaAuditing
@Configuration
public class JPAConfig {

}
