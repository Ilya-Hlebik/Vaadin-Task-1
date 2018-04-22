package com.gp.vaadin.demo.hotel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.server.SerializableFunction;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelEditForm extends FormLayout {

	private ViewForHotel ui;
	private HotelService hotelService = HotelService.getInstance();
	private Hotel hotel;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating= new TextField("Rating");
	private DateField operatesFrom = new DateField("Date");
	private NativeSelect<String> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");

    public Binder<Hotel> getHotelBinder() {
		return binder;
	}

	public void setHotelBinder(Binder<Hotel> hotelBinder) {
		this.binder = hotelBinder;
	}

	public Hotel getHotel() {
		return hotel;
	}
    private Button save = new Button("Save");
    private Button close = new Button("Close");
	private CategoryService categoryService = CategoryService.getInstance();

    public HotelEditForm(ViewForHotel ui) {

        this.ui = ui;
        setMargin(true);   
        setVisible(false);    
        HorizontalLayout buttons = new HorizontalLayout(save, close);
        buttons.setSpacing(true);
        addComponents(name,address,rating,operatesFrom,category,description, url, buttons);
        
    	name.setDescription("Hotel name");
		name.setWidth(100, Unit.PERCENTAGE);
		address.setWidth(100, Unit.PERCENTAGE);
		address.setDescription("Address of Hotel");
		rating.setWidth(100, Unit.PERCENTAGE);
		rating.setDescription("Rating of Hotel");
		operatesFrom.setWidth(100, Unit.PERCENTAGE);
		operatesFrom.setDescription("Date of Operates");
		category.setWidth(100, Unit.PERCENTAGE);
		category.setDescription("Category of Hotel");
		description.setWidth(100, Unit.PERCENTAGE);
		description.setDescription("Description of Hotel");
		url.setWidth(100, Unit.PERCENTAGE);
		url.setDescription("URL of Hotel");
		
        prepareFields();
        
        category.setItems( CategoryService.getInstance().findAll()  .stream() .map(Category::getName).collect(Collectors.toList()));
 
        save.addClickListener(e -> save());
        close.addClickListener(e -> {this.setVisible(false);
        ui.getAddHotel().setEnabled(true);
        ui.getHotelGrid().deselectAll();
        ui.updateHotel();});

    }
	@SuppressWarnings("serial")
	private void prepareFields() {
	 
	        binder.forField(name).asRequired("Please enter a name").bind(Hotel::getName, Hotel::setName);
	        binder.forField(address).asRequired("Please enter a address").bind(Hotel::getAddress, Hotel::setAddress);
	        binder.forField(rating).asRequired("Please enter a rating").withConverter(new StringToIntegerConverter("Please put an integer")).withValidator(e -> e >= 0 && e <= 5,"Enter number between 0 and 5").bind(Hotel::getRating, Hotel::setRating);
	        binder.forField(operatesFrom).asRequired("Please choese a date").withValidator( new DateRangeValidator("Date must be in the past", null, LocalDate.now())).withConverter(LocalDate::toEpochDay, LocalDate::ofEpochDay,  "error").bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
	        SerializableFunction<String,Category> toModel = ( e -> {return CategoryService.getInstance().findAll().stream()
	                                              .filter( f -> f.getName().equalsIgnoreCase(e))
	                                              .findAny().orElse(new Category(e));});
	        SerializableFunction<Category,String> toPresentation = (Category::getName);
	        SerializablePredicate<? super String> categoryValidator = ( e -> { return	categoryService.findAll()
	         .stream().map( cat -> cat.getName().toUpperCase()).collect(Collectors.toList())
	         .contains(new Category(e).getName() != null? new Category(e) .getName().toUpperCase(): "");
	        });
	        binder.forField(category).asRequired("Please chose a categhory") .withValidator(categoryValidator,"Error").withConverter(toModel, toPresentation, "chose category").bind(Hotel::getCategory, Hotel::setCategory);
	        binder.forField(url) .asRequired("Every hotel must have a link to booking.com").bind(Hotel::getUrl, Hotel::setUrl);
	        binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
		
	}
    public void save() {
        BinderValidationStatus<Hotel> result = binder.validate();
        if ( !result.hasErrors() ) {
            if (hotelService.save(getHotel())) {
                hotelService.save(getHotel());
                ui.updateHotel();
                this.setVisible(false);
            } else Notification.show("Can't save this " + getHotel().getName(),  Notification.Type.ERROR_MESSAGE);
            
        }
        ui.getAddHotel().setEnabled(true);
    }

    public void setHotel(Hotel hotel) {
        this.setVisible(true);
        this.hotel = hotel;
        binder.setBean(hotel);
    }

 

}

 