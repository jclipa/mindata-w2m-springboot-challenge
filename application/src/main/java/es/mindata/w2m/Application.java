package es.mindata.w2m;

import static es.mindata.w2m.constants.AppConstants.PACKAGE_BASE;
import static es.mindata.w2m.constants.AppConstants.PACKAGE_DOMAIN;
import static es.mindata.w2m.constants.AppConstants.PACKAGE_REPOSITORY;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication(scanBasePackages = { PACKAGE_BASE })
@EntityScan(basePackages = { PACKAGE_DOMAIN })
@EnableJpaRepositories(basePackages = { PACKAGE_REPOSITORY })
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "M2W-Challenge API", version = "1.0"))
@SecurityScheme(name = "user", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class Application {

	public static void main(String[] args) {
		log.info("Starting application...");
		SpringApplication.run(Application.class, args);
	}

}
