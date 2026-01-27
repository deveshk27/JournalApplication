package com.dkdev.Journal.Application.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserDTO {
    @NotEmpty
    private String username ;
    private String email ;
    private boolean sentimentAnalysis ;
    @NotEmpty
    private String password ;
}
