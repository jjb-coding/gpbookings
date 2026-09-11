package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import services.mapperService.MapperService;
import services.mapperService.MapperServiceSingleton;
import status.Status;
import ui.component.components.XComboBoxMonth;
import ui.component.components.XComboBoxYear;
import ui.component.components.XScrollBookings;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.container.ViewBag;
import ui.injectables.GetAttendedBookingRequest;
import ui.injectables.RescheduleBookingRequest;
import ui.object.EnumeratedItem;
import ui.object.NumberItem;
import ui.object.objects.BookingUI;

public class ViewBookingsController extends InjectableObject {
	// Singletons
	MapperService _mapperService;
	
	// Components
	XComboBoxMonth monthField;
	XComboBoxYear yearField;
	XScrollBookings scrollBookings;
	
	public ViewBookingsController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
		// Singletons
		_mapperService = MapperServiceSingleton.INSTANCE.get();
	}
	
	public void attach(
    		XComboBoxMonth monthField,
    		XComboBoxYear yearField,
    		XScrollBookings scrollBookings,
    		JButton refreshButton,
    		JButton closeButton
			) {

		// Assign
		this.monthField = monthField;
		this.yearField = yearField;
		this.scrollBookings = scrollBookings;
		
		// Load
		load();
		
        // Refresh button
        refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
        });
        
        // Close button
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(Status.DASHBOARD);
			}
        });
        
        // Month field change
        monthField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
        });
        
        // Year field change
        yearField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
        });
	}
	
	/**
	 * Loads the table for the first time, or refreshes it.
	 */
	public void load() {
		// Get the identifiers of selected items, if any
		EnumeratedItem month = (EnumeratedItem)monthField.getSelectedItem();
		NumberItem year = (NumberItem)yearField.getSelectedItem();
		if (month == null || year == null)
			return;
		
		// Call API safely
        api.output.ListBookingsOutput api = null;
        try {
        	api = new api.input.ListBookings(month.id(), year.id()).execute();
        }
        catch (DatabaseException | NoConnectionException e1) {
        	handleError(e1);
        }
        if (isBadStatus(api.status()))
        	return;
        
        // Map bookings from result to UI objects
		ArrayList<BookingUI> bookings = _mapperService.map(api.bookings(), api.outputResult.ListBookingsResult.class, BookingUI.class);
		
		// Set data on scroll bookings
		scrollBookings.setData(bookings);
	}
	
	/**
	 * Called by the bookings panel.
	 * @param bookingUUID	The booking to navigate to.
	 */
	public void rescheduleBooking(String bookingUUID) {
		// Configure the ViewBag
		ViewBag bag = _appContainer.createViewBag(InjectableEnum.RESCHEDULE_BOOKING_REQUEST);
		((RescheduleBookingRequest)bag.instance()).configure(bookingUUID);
		
		// Transition
		setStatus(Status.RESCHEDULE_BOOKING, bag);
	}
	
	/**
	 * Called by the bookings panel.
	 * @param bookingUUID	The booking to navigate to.
	 */
	public void viewAttendedBooking(String bookingUUID) {
		// Configure the ViewBag
		ViewBag bag = _appContainer.createViewBag(InjectableEnum.GET_ATTENDED_BOOKING_REQUEST);
		((GetAttendedBookingRequest)bag.instance()).configure(bookingUUID);
		
		// Transition
		setStatus(Status.VIEW_ATTENDED_BOOKING, bag);
	}
}
