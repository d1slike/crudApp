package ru.disdev.entity

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ru.disdev.entity.input.ComboBox
import ru.disdev.entity.input.TextField

class PollsFilter {
    @TextField(name = "Название опроса", description = "Название опроса", type = Type.STRING)
    @Filter
    val title = SimpleStringProperty()
    @TextField(name = "Категория", description = "Название категории", type = Type.STRING)
    @Filter
    val category = SimpleStringProperty()
}

class QuestionsFilter {
    @TextField(name = "Вопрос", description = "Заголовок вопроса", type = Type.STRING)
    @Filter
    val title = SimpleStringProperty()
    @ComboBox(name = "Опрос", description = "Заголовок опроса")
    @ValueSource(methodName = "pollId")
    @Filter(operator = Operator.EQ)
    val pollId = SimpleObjectProperty<ForeignKey>()
}

class AnswerFilter {
    @TextField(name = "Текст ответа", description = "Текст ответа", type = Type.STRING)
    @Filter
    val title = SimpleStringProperty()
    @ComboBox(name = "Вопрос", description = "Текст вопроса")
    @ValueSource(methodName = "questionId")
    @Filter(operator = Operator.EQ)
    val questionId = SimpleObjectProperty<ForeignKey>()
}

class UserFilter {
    @TextField(name = "ФИО", type = Type.STRING)
    @Column(name = "ФИО", description = "ФИО", type = Type.STRING)
    @Filter
    val fio = SimpleStringProperty()
    @ComboBox(name = "Пол")
    @ru.disdev.entity.Enum(Sex::class)
    @Filter(operator = Operator.EQ)
    val sex = SimpleObjectProperty<Sex>()
}

class LinkFilter {
    @ComboBox(name = "Вопрос")
    @ValueSource(methodName = "questionId")
    @Filter(operator = Operator.EQ)
    val question = SimpleObjectProperty<ForeignKey>()
    @ComboBox(name = "Ответ")
    @ValueSource(methodName = "answerId")
    @Filter(operator = Operator.EQ)
    val answer = SimpleObjectProperty<ForeignKey>()
    @ComboBox(name = "Опрошенный")
    @ValueSource(methodName = "userId")
    @Filter(operator = Operator.EQ)
    val user = SimpleObjectProperty<ForeignKey>()
}
