package com.gp.vaadin.demo.hotel;
 
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public class ComponentMenu extends CustomComponent {

    public ComponentMenu() {
        HorizontalLayout layout = new HorizontalLayout();
        MenuBar menu = new MenuBar();
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        layout.addComponent(menu);
        menu.addItem("Hotel", VaadinIcons.BUILDING, command());
        menu.addItem("Category", VaadinIcons.ACADEMY_CAP, command());
        layout.setSizeUndefined();
        setCompositionRoot(layout);
    }
    private Command command() {
        return new Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
            	if(selectedItem.getText().equals("Hotel"))
                getUI().getNavigator().navigateTo("");
            	else if(selectedItem.getText()=="Category")     getUI().getNavigator().navigateTo("category");
            }
        };
    }
}