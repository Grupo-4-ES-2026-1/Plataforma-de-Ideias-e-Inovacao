package PlataformaIdeiasInovacao.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String password
) {}