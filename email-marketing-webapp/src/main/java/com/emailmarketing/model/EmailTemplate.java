package com.emailmarketing.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model template email (subject + isi HTML).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {

    @NotBlank(message = "Subject wajib diisi")
    private String subject;

    @NotBlank(message = "Isi email (HTML) wajib diisi")
    private String htmlContent;
}
