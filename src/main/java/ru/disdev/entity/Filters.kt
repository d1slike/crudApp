package ru.disdev.entity

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ru.disdev.entity.input.ComboBox
import ru.disdev.entity.input.TextField

class PollsFilter {
    @TextField(name = "Название опроса", description = "Название опроса", type = Type.STRING, isRequired = false)
    @Filter
    var title = SimpleStringProperty("")
    @TextField(name = "Категория", description = "Название категории", type = Type.STRING, isRequired = false)
    @Filter
    var category = SimpleStringProperty("")
}

class QuestionsFilter {
    @TextField(name = "Вопрос", description = "Заголовок вопроса", type = Type.STRING, isRequired = false)
    @Filter
    val title = SimpleStringProperty("")
    @ComboBox(name = "Опрос", description = "Заголовок опроса", defaultFirst = false)
    @ValueSource(methodName = "pollId")
    @Filter(operator = Operator.EQ)
    val pollId = SimpleObjectProperty<ForeignKey>()
}

class AnswerFilter {
    @TextField(name = "Текст ответа", description = "Текст ответа", type = Type.STRING, isRequired = false)
    @Filter
    val title = SimpleStringProperty("")
    @ComboBox(name = "Вопрос", description = "Текст вопроса", defaultFirst = false)
    @ValueSource(methodName = "questionId")
    @Filter(operator = Operator.EQ, fieldName = "question_id")
    val questionId = SimpleObjectProperty<ForeignKey>()
}

class UserFilter {
    @TextField(name = "ФИО", type = Type.STRING, isRequired = false)
    @Filter
    val fio = SimpleStringProperty("")
    @ComboBox(name = "Пол", defaultFirst = false)
    @ru.disdev.entity.Enum(Sex::class)
    @Filter(operator = Operator.EQ)
    val sex = SimpleObjectProperty<Sex>()
}

class LinkFilter {
    @ComboBox(name = "Вопрос", defaultFirst = false)
    @ValueSource(methodName = "questionId")
    @Filter(operator = Operator.EQ, fieldName = "question_id")
    val question = SimpleObjectProperty<ForeignKey>()
    @ComboBox(name = "Ответ", defaultFirst = false)
    @ValueSource(methodName = "answerId")
    @Filter(operator = Operator.EQ, fieldName = "answer_id")
    val answer = SimpleObjectProperty<ForeignKey>()
    @ComboBox(name = "Опрошенный", defaultFirst = false)
    @ValueSource(methodName = "userId")
    @Filter(operator = Operator.EQ, fieldName = "user_id")
    val user = SimpleObjectProperty<ForeignKey>()
}
