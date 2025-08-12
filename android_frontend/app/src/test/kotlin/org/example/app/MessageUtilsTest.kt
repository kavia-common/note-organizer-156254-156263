package org.example.app

import org.junit.Test
import org.junit.Assert.assertEquals

class MessageUtilsTest {
    @Test
    fun testGetMessage() {
        // Match the current implementation of MessageUtils.message()
        assertEquals("Hello     World!", MessageUtils.message())
    }
}
