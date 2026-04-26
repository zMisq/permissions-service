package me.sarah.permissionservice;

import java.util.function.Consumer;

public class Test {

    void someFunction(Consumer<Integer> consumer) {
        var userExists = true;
        if (userExists) {
            consumer.accept(2);
        } else {
        }
    }

}
