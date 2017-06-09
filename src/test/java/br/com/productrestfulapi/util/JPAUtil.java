package br.com.productrestfulapi.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by juliano on 09/06/17.
 */
public class JPAUtil {

    private static final String ARQUIVO_CONEXAO_BD = "conexao.properties";

    public static EntityManager createEntityManager() throws IOException {
        Map cfg = new HashMap<String,String>();
        Properties arquivoConexao = new Properties();
        arquivoConexao.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(ARQUIVO_CONEXAO_BD));
        cfg.put("javax.persistence.jdbc.driver", arquivoConexao.getProperty("bd.productrestfulapi.driver"));
        cfg.put("javax.persistence.jdbc.url", arquivoConexao.getProperty("bd.productrestfulapi.url"));
        cfg.put("javax.persistence.jdbc.user", arquivoConexao.getProperty("bd.productrestfulapi.user"));
        cfg.put("javax.persistence.jdbc.password", arquivoConexao.getProperty("bd.productrestfulapi.password"));
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("productrestfulapi", cfg);
        return factory.createEntityManager();
    }
}
