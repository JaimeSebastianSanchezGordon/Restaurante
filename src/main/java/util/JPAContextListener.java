package util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JPAContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Inicialización se hace automáticamente en JPAUtil
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}