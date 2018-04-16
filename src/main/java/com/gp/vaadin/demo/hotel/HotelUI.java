package com.gp.vaadin.demo.hotel;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.AbstractRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class HotelUI extends UI {
	final VerticalLayout layout = new VerticalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final TextField filter = new TextField("Find by name");
	final TextField filter2 = new TextField("Find by address");
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	private HotelEditForm form = new HotelEditForm(this);
	final Hotel hotel = new Hotel();
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	HorizontalLayout controls = new HorizontalLayout();
    	controls.addComponents(filter, filter2, addHotel, deleteHotel);
    	
    	deleteHotel.setEnabled(false);
    	deleteHotel.addClickListener(e ->{
    		Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
    		hotelService.delete(delCandidate);
    		deleteHotel.setEnabled(false);
    		updateList();
    		form.setVisible(false);
    	});
    	
    	HorizontalLayout content = new  HorizontalLayout();
    	content.addComponents(hotelGrid,form);
    	form.setVisible(false);
    	
    	 
       hotelGrid.setItems(hotelService.findAll());
       hotelGrid.removeColumn("url"); 
       
       Column<Hotel,?> htmlColumn = hotelGrid.addColumn(e ->
       "<a href='" + e.getUrl() + "' target='_top'>"+e.getUrl()+"</a>",
       new HtmlRenderer()).setCaption("Url");
    	hotelGrid.setColumnOrder("name", "address", "rating", "category");
    	//hotelGrid.setWidth(100, Unit.PERCENTAGE);
    	hotelGrid.asSingleSelect().addValueChangeListener(e -> {
    		if(e.getValue() != null) {
    			deleteHotel.setEnabled(true);
    			form.setHotel(e.getValue());}});
    	
    	filter.addValueChangeListener(e -> updateList());
    	filter.setValueChangeMode(ValueChangeMode.LAZY);
    	
    	filter2.addValueChangeListener(e -> updateList());
    	filter2.setValueChangeMode(ValueChangeMode.LAZY);
    	
    	addHotel.addClickListener(e -> form.setHotel(new Hotel()));
    	
        layout.addComponents(controls, content);
        setContent(layout);
    }

    public void updateList() {
    	List<Hotel> hotelList = hotelService.findAll(filter.getValue(),filter2.getValue());
    	hotelGrid.setItems(hotelList);
    }
    
    
    @WebServlet(urlPatterns = "/*", name = "DemoUiServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class DemoUiServlet extends VaadinServlet {
    }
}
