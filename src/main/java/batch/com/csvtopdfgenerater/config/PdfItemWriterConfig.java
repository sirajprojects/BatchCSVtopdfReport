package batch.com.csvtopdfgenerater.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import batch.com.csvtopdfgenerater.writer.PdfItemWriter;

@Configuration
public class PdfItemWriterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfItemWriterConfig.class);

    @Bean
    public PdfItemWriter pdfItemWriter() {
        LOGGER.info("Initializing PdfItemWriter.");
        try {
            return new PdfItemWriter();
        } catch (Exception e) {
            LOGGER.error("Error initializing PdfItemWriter: ", e);
            throw new IllegalStateException("Failed to initialize PdfItemWriter", e);
        }
    }
}
