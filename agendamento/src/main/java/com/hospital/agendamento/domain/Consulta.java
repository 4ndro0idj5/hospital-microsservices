package com.hospital.agendamento.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "consultas")
@Getter
@Setter
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pacienteId;
    private Long medicoId;
    private String motivo;
    private OffsetDateTime dataHora;
    private String status;

}

