package com.project.controllerServlets.validator;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class ValidatorKey {

    Class<?> clazz;
    String method;
}
