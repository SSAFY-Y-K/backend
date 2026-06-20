package com.ssafy.passit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {
    private int totalSubmissions;
    private int acCount;
    private int attemptedProblems;
    private int solvedProblems;
}
