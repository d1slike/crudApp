package ru.disdev.tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TableDataService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 6);
                int i[] = new int[1];
                updateProgress(6, 6);
                return null;
            }

        };
    }

}

