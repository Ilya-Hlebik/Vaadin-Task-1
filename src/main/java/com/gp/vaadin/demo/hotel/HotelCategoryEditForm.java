package com.gp.vaadin.demo.hotel;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class HotelCategoryEditForm extends FormLayout {
		private Binder<Category> binder = new Binder<>(Category.class);
	    private Button save = new Button("Save");
	    private Button close = new Button("Close");
	    private TextField name = new TextField("Category");
	    private ViewForCategory viewForCategory;
	    private Category category;
	    private HorizontalLayout buttons = new HorizontalLayout(save, close);
	    public Category getCategory() {
			return category;
		}

	public HotelCategoryEditForm(ViewForCategory viewForCategory){
        this.viewForCategory = viewForCategory;
        addComponents(name, buttons);
        setMargin(true);   
        setVisible(false);
 
        name.setDescription("Category of Hotel");
        binder.forField(name).asRequired("Need name").bind(Category::getName, Category::setName);
        save.addClickListener(e -> save());
        close.addClickListener(e ->   {    this.setVisible(false);
        viewForCategory.getAddCategory().setEnabled(true);
        viewForCategory.getlistOfCategory().deselectAll();
        viewForCategory.updateCategoryList();});
    }

    public void save() {
        BinderValidationStatus<Category> result = binder.validate();
        if (result.hasErrors()) { Notification.show("Zero value", Notification.Type.WARNING_MESSAGE);}
        if ( !result.hasErrors() ) {
            if (CategoryService.getInstance().save(getCategory())) {
                viewForCategory.updateCategoryList();
                this.setVisible(false);
            } else {
                Notification.show("Please Enter a name of category  " , Notification.Type.ERROR_MESSAGE); }
        }
        viewForCategory.getAddCategory().setEnabled(true);
    }

    public void setCategory(Category category) {
    	 this.category = category;
        setVisible(true);
        binder.setBean(category);
    }
}