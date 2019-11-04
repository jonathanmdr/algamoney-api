package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.algaworks.algamoney.api.mail.Mailer;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.UsuarioRepository;
import com.algaworks.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.storage.S3;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LancamentoService {

    private static final String PERMISSAO_DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Mailer mailer;

    @Autowired
    private S3 s3;

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        if (logger.isDebugEnabled()) {
            logger.debug("Preparando envio de e-mail: Lançamentos Vencidos.");
        }

        List<Lancamento> lancamentosVencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if (lancamentosVencidos.isEmpty()) {
            logger.info("Não existem lançamentos vencidos para notificar.");
            return;
        }

        logger.info("Existem {} lançamentos vencidos para notificar.", lancamentosVencidos.size());

        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(PERMISSAO_DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warn("Existem lançamentos vencidos porém não existem usuários com permissão válida para receber a notificação por e-mail.");
        }

        mailer.avisarSobrelancamentosVencidos(lancamentosVencidos, destinatarios);

        logger.info("Envio de e-mail de aviso sobre lançamentos vencidos enviado.");
    }

    public byte[] realtorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dadosRelatorio = lancamentoRepository.porPessoa(inicio, fim);

        Map<String, Object> parametrosRelatorio = new HashMap<>();
        parametrosRelatorio.put("DT_INICIO", Date.valueOf(inicio));
        parametrosRelatorio.put("DT_FIM", Date.valueOf(fim));
        parametrosRelatorio.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametrosRelatorio, new JRBeanCollectionDataSource(dadosRelatorio));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = validaERetornaPessoa(lancamento);

        if (StringUtils.hasText(lancamento.getAnexo())) {
            s3.salvarPermanente(lancamento.getAnexo());
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long id, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(id);

        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validaERetornaPessoa(lancamento);
        }

        if (StringUtils.isEmpty(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
            s3.excluir(lancamentoSalvo.getAnexo());
        } else if (StringUtils.hasLength(lancamento.getAnexo()) && !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
            s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private Pessoa validaERetornaPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;

        if (lancamento.getPessoa().getId() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getId());
        }

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return pessoa;
    }

    private Lancamento buscarLancamentoExistente(Long id) {
        Lancamento lancamentoSalvo = lancamentoRepository.findOne(id);

        if (lancamentoSalvo == null) {
            throw new IllegalArgumentException();
        }

        return lancamentoSalvo;
    }
}
