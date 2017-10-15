package ru.disdev.entity.crud;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.Column;
import ru.disdev.entity.Crud;
import ru.disdev.entity.Sex;
import ru.disdev.entity.Type;
import ru.disdev.entity.input.ComboBox;
import ru.disdev.entity.input.DatePicker;
import ru.disdev.entity.input.TextField;

import java.time.LocalDate;

public class User extends Crud {
    @TextField(name = "ФИО", type = Type.STRING)
    @Column(name = "ФИО", description = "ФИО", type = Type.STRING)
    private StringProperty fio = new SimpleStringProperty();
    @DatePicker(name = "Дата рождения", description = "Дата рождения")
    @Column(name = "Дата рождения", description = "Дата рождения", type = Type.OBJECT)
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    @ComboBox(name = "Пол")
    @Column(name = "Пол", description = "Пол", type = Type.OBJECT)
    @ru.disdev.entity.Enum(Sex.class)
    private ObjectProperty<Sex> sex = new SimpleObjectProperty<>();

    public String getFio() {
        return fio.get();
    }

    public StringProperty fioProperty() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public Sex getSex() {
        return sex.get();
    }

    public ObjectProperty<Sex> sexProperty() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex.set(sex);
    }
}
