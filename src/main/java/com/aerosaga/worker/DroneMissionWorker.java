package com.aerosaga.worker;

import com.aerosaga.activity.DeliveryActivityImpl;
import com.aerosaga.activity.DroneActivityImpl;
import com.aerosaga.activity.MissionStatusActivityImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DroneMissionWorker {

    private static final String TASK_QUEUE = "DRONE_MISSION_TASK_QUEUE";

    private final WorkflowClient workflowClient;
    private final MissionStatusActivityImpl missionStatusActivity;

    public DroneMissionWorker(
            WorkflowClient workflowClient,
            MissionStatusActivityImpl missionStatusActivity) {

        this.workflowClient = workflowClient;
        this.missionStatusActivity = missionStatusActivity;
    }

    @PostConstruct
    public void startWorker() {

        WorkerFactory factory =
                WorkerFactory.newInstance(workflowClient);

        Worker worker =
                factory.newWorker(TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(
                DroneMissionWorkflowImpl.class
        );

        worker.registerActivitiesImplementations(
                new DroneActivityImpl(),
                new DeliveryActivityImpl(),
                missionStatusActivity
        );

        factory.start();

        System.out.println(
                "Temporal Worker started on task queue: "
                        + TASK_QUEUE
        );
    }
}