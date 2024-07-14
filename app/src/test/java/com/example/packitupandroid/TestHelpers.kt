package com.example.packitupandroid

import junit.framework.TestCase.assertEquals

// TODO: make shared source set using shared module
fun <T : Any> assertSameExcept(expected: T, actual: T, vararg excludedProperty: String) {
    val expectedClass = expected::class.java
    val declaredFields = expectedClass.declaredFields.filter { it.name !in excludedProperty }

    declaredFields.forEach { field ->
        field.isAccessible = true
        val expectedValue = field.get(expected)
        val actualValue = field.get(actual)
        assertEquals(expectedValue, actualValue)
    }
}
