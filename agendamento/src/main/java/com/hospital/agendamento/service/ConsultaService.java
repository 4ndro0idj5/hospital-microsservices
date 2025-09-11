package com.hospital.agendamento.service;

import com.hospital.agendamento.domain.Consulta;
import com.hospital.agendamento.repository.ConsultaRepository;
import com.hospital.commons.dto.NotificacaoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository repo;
    private final RabbitTemplate rabbitTemplate;

    public static final String NOTIFICACAO_ROUTING_KEY = "notificacao-consulta";

    public Consulta buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada: " + id));
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return repo.findByPacienteId(pacienteId);
    }

    @Transactional
    public Consulta criarConsulta(Consulta c) {
        c.setStatus("SCHEDULED");
        Consulta saved = repo.save(c);

        NotificacaoMessage msg = new NotificacaoMessage(
                saved.getId(),
                saved.getPacienteId(),
                "Lembrete: sua consulta foi agendada",
                saved.getDataHora(),
                saved.getMedicoId()
        );

        rabbitTemplate.setMessageConverter(new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(NOTIFICACAO_ROUTING_KEY, msg);
        return saved;
    }

    @Transactional
    public Consulta atualizarConsulta(Long id, Consulta novo) {
        Consulta exist = repo.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada: " + id));
        exist.setDataHora(novo.getDataHora());
        exist.setMotivo(novo.getMotivo());
        exist.setMedicoId(novo.getMedicoId());
        exist.setStatus(novo.getStatus());
        Consulta updated = repo.save(exist);

        NotificacaoMessage msg = new NotificacaoMessage(
                updated.getId(),
                updated.getPacienteId(),
                "Atualização de consulta",
                updated.getDataHora(),
                updated.getMedicoId()
        );

        rabbitTemplate.setMessageConverter(new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(NOTIFICACAO_ROUTING_KEY, msg);

        return updated;
    }

    public List<Consulta> listarFuturas() {
        return repo.findByDataHoraAfterOrderByDataHoraAsc(OffsetDateTime.now());
    }
}

