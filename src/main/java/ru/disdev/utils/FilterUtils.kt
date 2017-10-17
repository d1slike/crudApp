package ru.disdev.utils

import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import org.apache.commons.lang3.reflect.FieldUtils
import ru.disdev.entity.Filter
import ru.disdev.entity.ForeignKey
import ru.disdev.entity.Operator

fun toQuery(obj: Any?): String {
    if (obj == null) {
        return ""
    }
    val clazz = obj::class.java
    val list = FieldUtils.getFieldsListWithAnnotation(clazz, Filter::class.java)
            .asSequence()
            .filter { ObservableValue::class.java.isAssignableFrom(it.type) }
            .map {
                it.isAccessible = true
                val filter = it.getAnnotation(Filter::class.java)
                val name = if (filter.fieldName.isBlank())
                    it.name else filter.fieldName
                val operator = filter.operator
                val value = FieldUtils.readField(it, obj)
                val operand: String? = when (value) {
                    is StringProperty -> value.value
                    is IntegerProperty -> value.value.toString()
                    is DoubleProperty -> value.value.toString()
                    is BooleanProperty -> value.value.toString()
                    is ObjectProperty<*> -> {
                        val propValue = value.value
                        var stringValue: String? = null
                        if (propValue != null) {
                            stringValue = when (propValue) {
                                is ForeignKey -> propValue.value
                                else -> propValue.toString()
                            }
                        }
                        stringValue
                    }
                    else -> null
                }
                if (operand != null && !operand.isBlank()) {
                    val part = when (operator) {
                        Operator.LIKE -> "LIKE %$operand%"
                        else -> "= $operand"
                    }
                    return@map "$name $part"
                }
                null
            }.filter { it != null }
            .toList()
    return if (list.isNotEmpty()) {
        list.joinToString(separator = " && ", prefix = " WHERE ")
    } else {
        ""
    }
}