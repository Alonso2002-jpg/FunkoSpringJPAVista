package org.develop.FunkoSpringJpa.config.storage;

import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StorageConfig {

    @Bean
    public CommandLineRunner init(StorageService storageService, @Value("${upload.delete") String deleteAll) {
        return args -> {
            if (deleteAll.equals("true")) {
                log.info("Borrando Archivos de Almacenamiento");
                storageService.deleteAll();
            }

            storageService.init();
        };
    }
}
