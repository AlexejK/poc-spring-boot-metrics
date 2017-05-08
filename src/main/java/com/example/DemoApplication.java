package com.example;

import com.codahale.metrics.MetricRegistry;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.boot.actuate.metrics.dropwizard.ReservoirFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    public class IndexController {

        @RequestMapping
        public String getIndex() {

            return "index, welcome!";
        }

        @RequestMapping("/ping")
        public String getPing() {

            return "Pong";
        }
    }

    // https://github.com/spring-projects/spring-boot/issues/4405
    @Bean
    DropwizardMetricServices dropwizardMetricServices(MetricRegistry metricRegistry,
                                                      ObjectProvider<ReservoirFactory> resFactoryProvider) {
        return new DropwizardMetricServices(metricRegistry, resFactoryProvider.getIfAvailable()) {
            @Override
            public void submit(String name, double value) {
                if (name.startsWith("response.")) {
                    super.submit("timer." + name, value);
                    super.submit("timer.response.all", value);
                } else {
                    super.submit(name, value);
                }
            }
        };
    }
}
