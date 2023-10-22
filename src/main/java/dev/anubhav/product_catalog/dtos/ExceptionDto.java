package dev.anubhav.product_catalog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDto {
    private String message;

    public static ExceptionDto body(String message) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(message);
        return exceptionDto;
    }
}
