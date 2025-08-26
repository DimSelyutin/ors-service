package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = "/data.sql")
class ServiceIntTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    void testGetAllSubscriptions_withRealDb() {
        SubscriptionFilterDTO filter = new SubscriptionFilterDTO();
        filter.setDescription("Monthly");
        List<SubscriptionDTO> result = subscriptionService.getAllSubscriptions(filter);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertEquals(result.get(0).getName(), "Monthly");
    }
}
