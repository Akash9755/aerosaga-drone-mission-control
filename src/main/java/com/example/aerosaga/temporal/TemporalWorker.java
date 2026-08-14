package com.example.aerosaga.temporal;

import com.example.aerosaga.activity.DeliveryActivityImpl;
import com.example.aerosaga.activity.DroneActivityImpl;
import com.example.aerosaga.workflow.DroneMissionWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemporalWorker {

    private final WorkflowClient workflowClient;
    private final DroneActivityImpl droneActivity;
    private final DeliveryActivityImpl deliveryActivity;

    @PostConstruct
    public void start() {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(TemporalConfig.TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(DroneMissionWorkflowImpl.class);
        worker.registerActivitiesImplementations(droneActivity, deliveryActivity);

        factory.start();
    }
}