package com.miniProject.AeroScale;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class student {
    String name;
    int age;
    Instant time;

}
