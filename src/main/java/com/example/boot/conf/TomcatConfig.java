package com.example.boot.conf;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.charset.Charset;

/**
 * tomcat配置
 */
@Configuration
public class TomcatConfig {

    @Bean
    public EmbeddedServletContainerFactory servletContainer(){
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setUriEncoding(Charset.forName("UTF-8"));
        factory.addAdditionalTomcatConnectors(createSslConnector());
        return factory;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File truststore = new File("D:\\keystore\\server.keystore");
            connector.setScheme("https");
            protocol.setSSLEnabled(true);
            connector.setSecure(true);
            connector.setPort(8443);
            protocol.setKeystoreFile(truststore.getAbsolutePath());
            protocol.setKeystorePass("111111");
            protocol.setKeystoreType("JKS");
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststoreProvider("SUN");
            protocol.setTruststoreType("JKS");
            protocol.setTruststorePass("111111");
            protocol.setClientAuth("true");
            return connector;
        } catch (Exception ex) {
            throw new IllegalStateException("cant access keystore: [" + "keystore" + "]  ", ex);
        }
    }
}
