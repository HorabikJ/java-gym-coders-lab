package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailDto {

    @NotBlank(message = "*Title can not be empty.")
    private String title;

    @NotBlank(message = "*Content can not be empty.")
    private String text;

}
