package com.gp.vaadin.demo.hotel;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

 
public class ViewForHotel extends VerticalLayout implements View {

    final HotelService hotelService = HotelService.getInstance();
    final TextField filterByName = new TextField();
    final TextField filterByAddress = new TextField();
    final Button addHotel = new Button("Add hotel");
    final Button deleteHotel = new Button("Delete hotel");
    final Button editHotel = new Button("Edit hotel");

 
    final Grid<Hotel> hotelGrid = new Grid<>();

    private HotelEditForm hotelEditForm = new HotelEditForm(this);

    public Button getAddHotel() {
		return addHotel;
	}

	public Button getDeleteHotel() {
		return deleteHotel;
	}

	public Button getEditHotel() {
		return editHotel;
	}

	public Grid<Hotel> getHotelGrid() {
		return hotelGrid;
	}

	public ViewForHotel() {
      
		filterByName.setPlaceholder("Filter by name");
        filterByName.addValueChangeListener(e -> updateHotel());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        filterByAddress.setPlaceholder("Filter by address");
        filterByAddress.addValueChangeListener(e -> updateHotel());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
 
        addHotel.addClickListener(e -> {
            addHotel.setEnabled(false);
            hotelEditForm.setHotel(new Hotel());
        } );       
        
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(e -> {
            hotelGrid.getSelectedItems().forEach(hotelService::delete);
            deleteHotel.setEnabled(false);
            addHotel.setEnabled(true);
            updateHotel();
        });
 
        editHotel.setEnabled(false);
        editHotel.addClickListener(e -> {
            Hotel editCandidate = hotelGrid.getSelectedItems().iterator().next();
            hotelEditForm.setHotel(editCandidate);
        });
 
        hotelGrid.addColumn(Hotel::getName).setCaption("Name");
        hotelGrid.addColumn(Hotel::getAddress).setCaption("Address");
        hotelGrid.addColumn(Hotel::getRating).setCaption("Rating");
        hotelGrid.addColumn(hotel -> LocalDate.ofEpochDay(hotel.getOperatesFrom())).setCaption("Operates from");
        hotelGrid.addColumn( e -> {
            return CategoryService.getInstance().findAll().contains(e.getCategory())  ? e.getCategory().getName()  : "No category";}).setCaption("Category");

        Grid.Column<Hotel, String> htmlColumn = hotelGrid.addColumn(hotel ->
                        "<a href='" + hotel.getUrl() + "' target='_blank'>hotel info</a>",
                new HtmlRenderer()).setCaption("Url");
        hotelGrid.addColumn(Hotel::getDescription).setCaption("Description");
        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);        
  
        hotelGrid.addSelectionListener(e -> {
         
            Set<Hotel> selectedHotels = e.getAllSelectedItems();
            if (selectedHotels != null && selectedHotels.size() == 1) {
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(true);
            } else if (selectedHotels != null && selectedHotels.size() > 1) {
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(false);
            } else {
                deleteHotel.setEnabled(false);
                editHotel.setEnabled(false);
                hotelEditForm.setVisible(false);
            }
        });
       updateHotel();
       
       	HorizontalLayout control = new HorizontalLayout(filterByName, filterByAddress, addHotel, deleteHotel, editHotel);
        HorizontalLayout content = new HorizontalLayout(hotelGrid, hotelEditForm);
        setSpacing(true);
        setMargin(false);
        setWidth("100%");
        hotelGrid.setSizeFull();          
        hotelEditForm.setSizeFull();
        content.setMargin(false);
        content.setWidth("100%");
        content.setHeight(32, Unit.REM);
        content.setExpandRatio(hotelGrid, 229);
        content.setExpandRatio(hotelEditForm, 92);
        addComponents(control, content);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void updateHotel() {
        List<Hotel> hotelList = hotelService.findAll(filterByName.getValue(),filterByAddress.getValue());
        this.hotelGrid.setItems(hotelList);
    }

}