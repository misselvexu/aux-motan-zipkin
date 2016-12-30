package io.github.ivetech.auxiliaries.motan.brave;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BraveContextAware implements ApplicationContextAware {

    static final long serialVersionUID = 02L;

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext () {
        return applicationContext;
    }

    @Override
    public void setApplicationContext (final ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

}
