package com.karim.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

@Configuration
public class HapiFhirConfiguration {

    @Bean
    @ApplicationScope
    FhirContext fhirContext(){
        FhirContext ctx=FhirContext.forR4();
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        // Set how long to try and establish the initial TCP connection (in ms)
        ctx.getRestfulClientFactory().setConnectTimeout(20 * 1000);
        // Set how long to block for individual read/write operations (in ms)
        ctx.getRestfulClientFactory().setSocketTimeout(50 * 1000);

        return ctx;
    }

    @Bean
    @ApplicationScope
    IParser fhirJsonPasrser(FhirContext fhirContext){
        return fhirContext.newJsonParser().setPrettyPrint(true);
    }

    @Bean
    @ApplicationScope
    IGenericClient iGenericClient(FhirContext fhirContext){
        return fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4/");
    }
}
