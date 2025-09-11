package com.hospital.agendamento.repository;

import com.hospital.agendamento.domain.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByDataHoraAfterOrderByDataHoraAsc(OffsetDateTime now);
}
