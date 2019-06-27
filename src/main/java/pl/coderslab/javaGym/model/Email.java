package pl.coderslab.javaGym.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Email {

    @NotBlank(message = "*Title can not be empty.")
    private String title;

    @NotBlank(message = "*Content can not be empty.")
    private String text;

}
