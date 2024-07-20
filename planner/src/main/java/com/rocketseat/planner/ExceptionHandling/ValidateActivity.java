package com.rocketseat.planner.ExceptionHandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateActivity {

    public void ValidateActivity(String occurs_at_activity, LocalDateTime starts_at_trip, LocalDateTime ends_at_Trip) {

        if (occurs_at_activity == null || occurs_at_activity.isBlank() || occurs_at_activity.isEmpty()) {
            throw new InvalidExceptionMessage("Datas de início das atividades são obrigatórias.");
        }

        try {
            // Tentar converter as strings para LocalDateTime
            LocalDateTime startDateActivity = LocalDateTime.parse(occurs_at_activity, DateTimeFormatter.ISO_DATE_TIME);

            // Verificar se a data de início da atividade está entre as datas da viagem
            if (!startDateActivity.isAfter(starts_at_trip) || !startDateActivity.isBefore(ends_at_Trip)) {
                throw new InvalidExceptionMessage("A data de início da Atividade deve estar " +
                        "entre as datas da viagem" + starts_at_trip + ends_at_Trip);
            }

        } catch (
                DateTimeParseException e) {
            // Lançar exceção se o formato da data for inválido
            throw new InvalidExceptionMessage("Formato de data inválido. Use o formato ISO 8601 (yyyy-MM-ddTHH:mm:ss).");
        }


    }
}
