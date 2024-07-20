package com.rocketseat.planner.ExceptionHandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateDates{

    public void validateDates(String startsAt, String endsAt) {
        // Verificar se as datas são nulas ou vazias
        if (startsAt == null || startsAt.isEmpty() || endsAt == null || endsAt.isEmpty()) {
            throw new InvalidExceptionMessage("Datas de início e término são obrigatórias.");
        }

        try {
            // Tentar converter as strings para LocalDateTime
            LocalDateTime startDate =  LocalDateTime.parse(startsAt, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endDate = LocalDateTime.parse(endsAt, DateTimeFormatter.ISO_DATE_TIME);

            // Verificar se a data de início é anterior à data de término
            if (startDate.isAfter(endDate)) {
                throw new InvalidExceptionMessage("A data de início deve ser anterior à data de término.");
            }

            // Verificar se a data de início é no futuro
            if (startDate.isBefore(LocalDateTime.now())) {
                throw new InvalidExceptionMessage("A data de início não pode ser no passado.");
            }

        } catch (DateTimeParseException e) {
            // Lançar exceção se o formato da data for inválido
            throw new InvalidExceptionMessage("Formato de data inválido. Use o formato ISO 8601 (yyyy-MM-ddTHH:mm:ss).");
        }
    }

}
