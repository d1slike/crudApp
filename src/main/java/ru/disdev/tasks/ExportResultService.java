package ru.disdev.tasks;

import au.com.bytecode.opencsv.CSVWriter;
import javafx.beans.property.Property;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Crud;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static ru.disdev.utils.IOUtils.getWin1251FileWriter;

public class ExportResultService extends Service<Void> {

    private final List<? extends Crud> resultsToExport;
    private final File file;

    public ExportResultService(List<? extends Crud> resultsToExport, File file) {
        this.resultsToExport = resultsToExport;
        this.file = file;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File target = new File(file.getAbsolutePath() + "/results.csv");
                try (CSVWriter writer = new CSVWriter(getWin1251FileWriter(target), ';')) {
                    writer.writeNext(makeTableHeader());
                    resultsToExport.stream()
                            .map(ExportResultService.this::toCSVRow)
                            .forEach(writer::writeNext);
                }
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private String[] toCSVRow(Crud result) {
        List<String> list = FieldUtils.getFieldsListWithAnnotation(result.getClass(), Column.class)
                .stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Property<Object> prop = (Property<Object>) FieldUtils.readField(field, result);
                        Object value = prop.getValue();
                        return value == null ? "" : value.toString();
                    } catch (Exception ignored) {
                    }
                    return "";
                }).collect(Collectors.toList());
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    private String[] makeTableHeader() {
        List<String> list = FieldUtils.getFieldsListWithAnnotation(resultsToExport.get(0).getClass(), Column.class)
                .stream()
                .map(field -> {
                    Column annotation = field.getAnnotation(Column.class);
                    String columnName = annotation.csvColumnName();
                    if (columnName.isEmpty()) {
                        columnName = annotation.name();
                    }
                    return columnName;
                })
                .collect(Collectors.toList());
        String[] array = new String[list.size()];
        return list.toArray(array);
    }
}
