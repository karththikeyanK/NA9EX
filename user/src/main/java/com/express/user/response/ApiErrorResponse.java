package com.express.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";

    private String status;
    private String msg;
}
