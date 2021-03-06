package com.algaworks.algamoney.api.mail;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    public void avisarSobrelancamentosVencidos(List<Lancamento> lancamentosVencidos, List<Usuario> destinatarios) {
        String template = "mail/aviso-lancamentos-vencidos";

        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("lancamentos", lancamentosVencidos);

        List<String> emailsDestinatarios = destinatarios.stream().map(usuario -> usuario.getEmail()).collect(Collectors.toList());

        this.enviarEmail("devsoftsta@gmail.com", emailsDestinatarios, "Lançamentos Vencidos", template, variaveis);
    }

    private void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
        Context context = new Context(new Locale("pt", "BR"));

        variaveis.entrySet().forEach(variavel -> context.setVariable(variavel.getKey(), variavel.getValue()));

        String mensagem = thymeleaf.process(template, context);

        this.enviarEmail(remetente, destinatarios, assunto, mensagem);
    }

    private void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            mailSender.send(mimeMessage);
        } catch(MessagingException ex) {
            throw new RuntimeException("Não foi possível realizar o envio de e-mail!", ex);
        }
    }

    /*
        *** BLOCO PARA TESTAR ENVIO DE E-MAIL ***

        @Autowired
        private LancamentoRepository repository;

        @EventListener
        private void teste(ApplicationReadyEvent event) {
            String template = "mail/aviso-lancamentos-vencidos";

            List<Lancamento> lista = repository.findAll();
            Map<String, Object> variaveis = new HashMap<>();
            variaveis.put("lancamentos", lista);

            this.enviarEmail("devsoftsta@gmail.com",
                    Arrays.asList("jonathan.mdr@hotmail.com"),
                    "Testando",
                    template, variaveis);
        }
    */

}
