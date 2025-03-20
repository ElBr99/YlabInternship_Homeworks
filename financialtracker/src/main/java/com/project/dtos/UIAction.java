package com.project.dtos;

import java.util.stream.Stream;

public enum UIAction {
    action1(1),
    action2(2),
    action3(3),
    action4(4),
    action5(5),
    action6(6),
    action7(7),
    action8(8),
    action9(9),
    action10(10),
    action11(11),
    action12(12),
    action13(13),
    action14(14),
    action15(15),
    action16(16);


    private final int value;

    UIAction(int value) {
        this.value = value;
    }

    public static UIAction findByValue(int i) {
        return Stream.of(UIAction.values())
                .filter(el -> el.getValue() == i)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public int getValue() {
        return value;
    }
}
