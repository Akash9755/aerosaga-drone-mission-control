package com.aerosaga.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    public static final String TASK_QUEUE = "AERO_TASK_QUEUE";

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        // Points at the local Temporal cluster started via docker compose /
        // `temporal server start-dev` (default gRPC port 7233)
        return WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget("localhost:7233")
                        .build()
        );
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    }
}