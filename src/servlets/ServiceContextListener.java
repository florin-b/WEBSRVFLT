package servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import beans.GoogleContext;

public class ServiceContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		GoogleContext.getContext();

		GoogleContext.getContextDist();

	}

}
