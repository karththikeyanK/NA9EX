package com.express.user.projection;

import java.time.LocalDate;

public interface TicketProjection {
    Long getId();
    LocalDate getDate();
    int getMaleCount();
    int getFemaleCount();
    String getToWhere();
    String getStatus();
    String getMsg();
    String getDescription();
}
