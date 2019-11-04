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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Mailer mailer;

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        List<Lancamento> lancamentosVencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(PERMISSAO_DESTINATARIOS);

        mailer.avisarSobrelancamentosVencidos(lancamentosVencidos, destinatarios);
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
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getId());

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long id, Lancamento lancamento) {
        Lancamento lancamento1Salvo = buscarLancamentoExistente(id);

        if (!lancamento.getPessoa().equals(lancamento1Salvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamento1Salvo, "id");

        return lancamentoRepository.save(lancamento1Salvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;

        if (lancamento.getPessoa().getId() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getId());
        }

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long id) {
        Lancamento lancamentoSalvo = lancamentoRepository.findOne(id);

        if (lancamentoSalvo == null) {
            throw new IllegalArgumentException();
        }

        return lancamentoSalvo;
    }
}
