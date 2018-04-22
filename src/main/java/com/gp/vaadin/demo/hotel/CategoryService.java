package com.gp.vaadin.demo.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CategoryService {
    private static CategoryService instance;
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    private final HashMap<Long, Category> category = new HashMap<>();
    private long nextId = 0;

    private CategoryService() {
    }

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            instance.ensureTestData();
        }
        return instance;
    }

    public synchronized List<Category> findAll() {
        return findAll(null);
    }

	public synchronized List<Category> findAll(String filter) {
		ArrayList<Category> arrayList = new ArrayList<>();
		for (Category category : category.values()) {
			try {
				boolean passesFilter = passesFilter(filter, category.getName());
				if (passesFilter) {
					arrayList.add(category.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort( arrayList ,new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public boolean passesFilter(String filter, String value) {
	return	(filter == null || filter.isEmpty())|| value.toLowerCase().contains(filter.toLowerCase());
	}
	
	public synchronized long count() {
		return category.size();
	}

	public synchronized void delete(Category value) {
		category.remove(value.getId());
	}

	public synchronized boolean save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return false;
		}
		List<String> categoryList = this.findAll().stream().map(cat -> cat.getName().toUpperCase())
				.collect(Collectors.toList());
 
		if(categoryList.contains(entry.getName()!=null? entry.getName().toUpperCase():"")){
			return false;
		}
		
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Category) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		category.put(entry.getId(),entry);
		return true;
  }
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			for(HotelCategory category: HotelCategory.values()) {
			  this.save(new Category(category.name()));
			}
		 }
		}
}