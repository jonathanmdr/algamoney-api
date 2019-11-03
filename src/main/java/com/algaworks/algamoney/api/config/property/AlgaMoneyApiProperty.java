package com.algaworks.algamoney.api.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("algamoney")
public class AlgaMoneyApiProperty {

    private String originPermitida = "http://localhost:8000";
    private final Seguranca seguranca = new Seguranca();
    private final Mail mail = new Mail();

    @Getter
    @Setter
    public static class Seguranca {
        private boolean enableHttps;
    }

    @Getter
    @Setter
    public static class Mail {
        private String host;
        private Integer port;
        private String username;
        private String password;
    }

}
