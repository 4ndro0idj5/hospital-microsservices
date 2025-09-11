package com.hospital.notificacao.service;

import com.hospital.commons.dto.NotificacaoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoService {

    public void processarNotificacao(NotificacaoMessage mensagem) {
        log.info("[NOTIFICAÇÃO] Paciente: {} - {}", mensagem.pacienteId(), mensagem.mensagem());
        log.info("Data: {}, ConsultaId: {}, MedicoId: {}", mensagem.dataHora(), mensagem.consultaId(), mensagem.medicoId());


    }
}

