package com.algaworks.algamoney.api.resource;

import static com.algaworks.algamoney.api.token.utils.RefreshTokenProcessorUtils.COOKIE_NAME;
import static com.algaworks.algamoney.api.token.utils.RefreshTokenProcessorUtils.URI_TOKEN;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tokens")
public class TokenResource {

    @Autowired
    private AlgaMoneyApiProperty algaMoneyApiProperty;

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest req, HttpServletResponse res) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(algaMoneyApiProperty.getSeguranca().isEnableHttps());
        cookie.setPath(req.getContextPath().concat(URI_TOKEN));
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        res.setStatus(HttpStatus.NO_CONTENT.value());
    }

}
