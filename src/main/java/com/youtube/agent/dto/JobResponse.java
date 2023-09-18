package com.youtube.agent.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JobResponse {
    private String videoLink;
    private String comment;
}
