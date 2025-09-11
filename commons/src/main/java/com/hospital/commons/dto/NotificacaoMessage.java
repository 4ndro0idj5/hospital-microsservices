package com.hospital.commons.dto;

import java.time.OffsetDateTime;

public record NotificacaoMessage(
        Long consultaId,
        Long pacienteId,
        String mensagem,
        OffsetDateTime dataHora,
        Long medicoId
) {}
