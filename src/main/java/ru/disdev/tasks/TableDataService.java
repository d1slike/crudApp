package ru.disdev.tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.service.AnswerService;
import ru.disdev.service.PollService;
import ru.disdev.service.QuestionService;

public class TableDataService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 3);
                PollService.getInstance().load();
                updateProgress(1, 3);
                QuestionService.getInstance().load();
                updateProgress(2, 3);
                AnswerService.getInstance().load();
                updateProgress(3, 3);
                return null;
            }

            protected void updateProgress(long workDone, long max) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                }
                super.updateProgress(workDone, max);
            }
        };
    }

}

