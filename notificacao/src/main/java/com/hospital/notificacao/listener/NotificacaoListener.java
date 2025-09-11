package com.hospital.notificacao.listener;

import com.hospital.commons.dto.NotificacaoMessage;
import com.hospital.notificacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacaoListener {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = "notificacao-consulta")
    public void receberMensagem(NotificacaoMessage mensagem) {
        notificacaoService.processarNotificacao(mensagem);
    }
}
