package com.gp.vaadin.demo.hotel;

import java.util.Set;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ViewForCategory extends VerticalLayout implements View {
	
	private HotelCategoryEditForm categoryEditForm = new HotelCategoryEditForm(this);
    final Button addCategory = new Button("Add category");
    final Button deleteCategory = new Button("Delete category");
    final Button editCategory = new Button("Edit category");
    final CategoryService categoryService = CategoryService.getInstance();
    
    final Grid<Category> listOfCategory = new Grid<>();

    public Button getAddCategory() {
		return addCategory;
	}

	public Button getDeleteCategory() {
		return deleteCategory;
	}

	public Button getEditCategory() {
		return editCategory;
	}

	public Grid<Category> getlistOfCategory() {
		return listOfCategory;
	}

    public ViewForCategory() {
    	   addCategory.addClickListener(e -> {
               addCategory.setEnabled(false);
               categoryEditForm.setCategory(new Category(null)); } );
    	   
           deleteCategory.setEnabled(false);
           deleteCategory.addClickListener(e -> {
               listOfCategory.getSelectedItems().forEach(categoryService::delete);
               deleteCategory.setEnabled(false);
               addCategory.setEnabled(true);
               listOfCategory.setItems(categoryService.findAll()); });
           
           editCategory.setEnabled(false);
           editCategory.addClickListener(e -> {
               Category editCandidate = listOfCategory.getSelectedItems().iterator().next();
               categoryEditForm.setCategory(editCandidate);
           });
     
           listOfCategory.addColumn(Category::getName).setCaption("Name");
           listOfCategory.setSelectionMode(Grid.SelectionMode.MULTI);
    
           listOfCategory.addSelectionListener(e -> {
               Set<Category> selectedCategories = e.getAllSelectedItems();
               if (selectedCategories != null && selectedCategories.size() == 1) {
                   deleteCategory.setEnabled(true);
                   editCategory.setEnabled(true);
               } else if (selectedCategories != null && selectedCategories.size() > 1) {
                   deleteCategory.setEnabled(true);
                   editCategory.setEnabled(false);
               } else {
                   deleteCategory.setEnabled(false);
                   editCategory.setEnabled(false);
                   categoryEditForm.setVisible(false);
               }
           });
           setSpacing(true);
           setMargin(false);
           setWidth("100%");
           listOfCategory.setItems(categoryService.findAll());
           HorizontalLayout control = new HorizontalLayout(addCategory, deleteCategory, editCategory);
           HorizontalLayout content = new HorizontalLayout(listOfCategory, categoryEditForm);
           listOfCategory.setSizeFull();    
           categoryEditForm.setSizeFull();
           content.setMargin(false);
           content.setWidth("100%");
           addComponents(control, content);
         
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    public void updateCategoryList() {
       
      listOfCategory.setItems(categoryService.findAll());
    }
}