package com.project.controller;

import com.project.dtos.UIAction;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UIActionTest {

    @Test
    void findByValue_ExistingValue_ReturnsCorrectUIAction() {
        assertEquals(UIAction.action1, UIAction.findByValue(1));
        assertEquals(UIAction.action5, UIAction.findByValue(5));
        assertEquals(UIAction.action16, UIAction.findByValue(16));
    }

    @Test
    void findByValue_NonExistingValue_ThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> UIAction.findByValue(0));
        assertThrows(RuntimeException.class, () -> UIAction.findByValue(17));
        assertThrows(RuntimeException.class, () -> UIAction.findByValue(-1));
    }

    @Test
    void getValue_ReturnsCorrectValue() {
        assertEquals(1, UIAction.action1.getValue());
        assertEquals(8, UIAction.action8.getValue());
        assertEquals(16, UIAction.action16.getValue());
    }

    @Test
    void enumValues_ContainsAllActions() {
        UIAction[] actions = UIAction.values();
        assertEquals(16, actions.length);
        assertTrue(Stream.of(actions).anyMatch(action -> action == UIAction.action1));
        assertTrue(Stream.of(actions).anyMatch(action -> action == UIAction.action16));
    }
}