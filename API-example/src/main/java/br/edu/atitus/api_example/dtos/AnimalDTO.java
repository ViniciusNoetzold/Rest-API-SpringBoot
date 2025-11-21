package br.edu.atitus.api_example.dtos;

public record AnimalDTO(
        String nome,
        String descricao,
        double latitude,
        double longitude,
        String color // Cor do pino
) {
}