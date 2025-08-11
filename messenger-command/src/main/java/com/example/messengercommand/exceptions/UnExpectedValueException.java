package com.example.messengercommand.exceptions;

import com.example.messengerutilities.utility.DataTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ToString
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnExpectedValueException extends BaseException {
    public UnExpectedValueException(DataTypes dataType) {
        super("UnExpected data type : " + dataType.name(), HttpStatus.BAD_REQUEST);
    }
}
