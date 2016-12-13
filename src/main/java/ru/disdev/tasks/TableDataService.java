package ru.disdev.tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.datasource.ValueSource;
import ru.disdev.service.*;

import java.util.stream.Stream;

public class TableDataService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 6);
                int i[] = new int[1];
                Stream.of(LinkService.getInstance(), UserService.getInstance(),
                        PollService.getInstance(), AnswerService.getInstance(),
                        QuestionService.getInstance()).forEach(service -> {
                    service.load();
                    updateProgress(++i[0], 6);
                });
                ValueSource.update();
                updateProgress(6, 6);
                return null;
            }

        };
    }

}

