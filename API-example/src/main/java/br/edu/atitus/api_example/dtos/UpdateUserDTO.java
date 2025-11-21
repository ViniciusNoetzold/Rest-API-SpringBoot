package br.edu.atitus.api_example.dtos;

public record UpdateUserDTO(
        String name,
        String bio,
        String avatar // Vai receber: "dog", "cat", "bone", etc.
) {
}