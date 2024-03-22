package com.ruoyi.common.utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtils {

    private volatile static Validator validator;

    private ValidationUtils() {

    }
    public static Validator getValidator() {
        if (validator == null) {
            synchronized (ValidationUtils.class) {
                if (validator == null) {
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    validator = validatorFactory.getValidator();
                }
            }
        }
        return validator;
    }

}
