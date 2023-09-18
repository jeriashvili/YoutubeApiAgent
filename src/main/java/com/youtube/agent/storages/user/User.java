package com.youtube.agent.storages.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotEmpty(message = "Username is empty")
    private String username;
    @NotEmpty(message = "Password is empty")
    private String password;
    @NotEmpty(message = "Country code is empty")
    private String country;
    @NotNull(message = "Job time is empty")
    private Integer jobTimeInMinutes;
    private LocalDateTime creationDate;
}
