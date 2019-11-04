package com.algaworks.algamoney.api.storage;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AlgaMoneyApiProperty property;

    public String salvarTemporariamente(MultipartFile arquivo) {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(arquivo.getContentType());
        objectMetadata.setContentLength(arquivo.getSize());

        String nomeUnicoArquivo = gerarNomeunicoArquivo(arquivo.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    property.getS3().getBucket(),
                    nomeUnicoArquivo,
                    arquivo.getInputStream(),
                    objectMetadata).withAccessControlList(accessControlList);

            putObjectRequest.setTagging(new ObjectTagging(
                    Arrays.asList(new Tag("expirar", "true"))
            ));

            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled()) {
                logger.debug("Arquivo {} enviado com sucesso para o S3.", arquivo.getOriginalFilename());
            }

            return nomeUnicoArquivo;
        } catch (IOException ex) {
            throw  new RuntimeException("Não foi possível enviar o arquivo para o AWS Bucket S3.", ex);
        }

    }

    public void salvarPermanente(String nomeArquivo) {
        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
                property.getS3().getBucket(),
                nomeArquivo,
                new ObjectTagging(Collections.emptyList()));

        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public void excluir(String nomeArquivo) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(property.getS3().getBucket(), nomeArquivo);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void substituir(String nomeArquivoAntigo, String nomeNovoArquivo) {
        if (StringUtils.hasText(nomeArquivoAntigo)) {
            this.excluir(nomeArquivoAntigo);
        }

        this.salvarPermanente(nomeNovoArquivo);
    }

    public String configurarUrl(String nomeArquivo) {
        return "\\\\"+property.getS3().getBucket()+".s3.amazonaws.com/"+nomeArquivo;
    }

    private String gerarNomeunicoArquivo(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

}
