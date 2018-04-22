package com.gp.vaadin.demo.hotel;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

 
import com.vaadin.ui.VerticalLayout;

@PushStateNavigation 
public class Navigation extends UI {


	final VerticalLayout layout = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private Component componentMenu = new ComponentMenu();
    public VerticalLayout getContentPanel() {
		return content;
	}

    @Override
    protected void init(VaadinRequest request) {
    	   Navigator navigator = new Navigator(this, content);
           navigator.addView("",  ViewForHotel.class);
           navigator.addView("category", ViewForCategory.class);
           layout.setMargin(true);
           layout.setSpacing(true);
           content.setSizeFull();
           content.setMargin(false);
           layout.addComponents(componentMenu,content);
           setContent(layout);
    }
    
	public void setContentPanel(VerticalLayout contentPanel) {
		this.content = contentPanel;
	}
    @WebServlet(urlPatterns = "/*", name = "DemoUiServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = Navigation.class, productionMode = false)
    public static class DemoUIServlet extends VaadinServlet {
    }
}