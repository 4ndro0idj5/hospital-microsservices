package com.hospital.agendamento.graphql;

import com.hospital.agendamento.domain.Consulta;
import com.hospital.agendamento.service.ConsultaService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConsultaGraphQLController {

    private final ConsultaService service;

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public Consulta consultaById(@Argument Long id) {
        return service.buscarPorId(id);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    public List<Consulta> consultasByPaciente(@Argument Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public List<Consulta> consultasFuturas() {
        return service.listarFuturas();
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    public Consulta criarConsulta(@Argument("input") ConsultaInput input) {
        Consulta c = new Consulta();
        c.setPacienteId(input.getPacienteId());
        c.setMedicoId(input.getMedicoId());
        c.setMotivo(input.getMotivo());
        c.setDataHora(OffsetDateTime.parse(input.getDataHora()));
        return service.criarConsulta(c);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    public Consulta atualizarConsulta(@Argument Long id, @Argument("input") ConsultaInput input) {
        Consulta c = new Consulta();
        c.setPacienteId(input.getPacienteId());
        c.setMedicoId(input.getMedicoId());
        c.setMotivo(input.getMotivo());
        c.setDataHora(OffsetDateTime.parse(input.getDataHora()));
        c.setStatus(input.getStatus());
        return service.atualizarConsulta(id, c);
    }
    @Getter
    @Setter
    public static class ConsultaInput {
        private Long pacienteId;
        private Long medicoId;
        private String motivo;
        private String dataHora;
        private String status;
    }
}