package com.project.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"userEmail", "goal", "target", "current"})
public class Goal {
    int id;
    String userEmail;
    String goal;
    BigDecimal target;
    BigDecimal current;
}
