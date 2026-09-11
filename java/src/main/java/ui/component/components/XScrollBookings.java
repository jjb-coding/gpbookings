package ui.component.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ui.controllers.ViewBookingsController;
import ui.object.objects.BookingUI;
import ui.object.objects.WasAttendedEnumUI;
import ui.registry.ColourRegistry;
import ui.registry.ImageRegistry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * 
 */
public class XScrollBookings extends JScrollPane {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	// Attachments
	ViewBookingsController _controller;

	// Fields
	private XTableBookings table;
	private ArrayList<BookingUI> dataUI;
		
	/**
	 * Constructs an XScrollBookings instance. Sets to loading state
	 * by default.
	 */
	public XScrollBookings(ViewBookingsController _controller) {
		// Super
		super();
		
		// Attach controller
		this._controller = _controller;
		
		// Default: loading
		setLoading();
	}

	
	/**
	 * Set the table to loading state.
	 */
	public void setLoading() {
		// Clear the data
		dataUI = null;
		
		// Make the label
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel("Loading...");
		labelPanel.add(label);
		
		// Set viewport
		this.setViewportView(labelPanel);
	}
	
	/**
	 * Set table data.
	 * @param bookings
	 */
	public void setData(ArrayList<BookingUI> bookings) {
		// Bind the data
		dataUI = bookings;

		// If there are no bookings
		if (bookings.size() == 0) {
			setEmpty();
			return;
		}
		
		// Make the table if none exists
		if (table == null)
			table = new XTableBookings(this);
		
		// Set data on table
		table.setData(bookings);
		
		// Set viewport
		this.setViewportView(table);
	}
	
	/**
	 * Link row to object and pass on to controller.
	 * @param row	The row.
	 */
	public void attendedAt(int row) {
		_controller.viewAttendedBooking(dataUI.get(row).bookingUUID());
	}
	
	/**
	 * Link row to object and pass on to controller.
	 * @param row
	 */
	public void rescheduleAt(int row) {
		_controller.rescheduleBooking(dataUI.get(row).bookingUUID());
	}
	
	void setEmpty() {
		// Make the label
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel("There are no bookings for this month to display.");
		labelPanel.add(label);
		
		// Set viewport
		this.setViewportView(labelPanel);		
	}
}

/**
 * 
 */
class XTableBookings extends JTable {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Attachments
	XScrollBookings _parent;
	
	/**
	 * Set table data.
	 * @param bookings 
	 */
	public void setData(ArrayList<BookingUI> bookings) {
		// Get table model & empty
		DefaultTableModel model = (DefaultTableModel)getModel();
		while (model.getRowCount() > 0)
			model.removeRow(0);
		
		// For each booking
		for (BookingUI booking : bookings)
			model.addRow(booking.asObjects());
	}
	
	/**
	 * Constructor method. Constructs the default table model, adds columns,
	 * and assigns it to the table.
	 */
	private void constructDefaultTableModel() {
		// Create and assign the model
		DefaultTableModel model = new DefaultTableModel() {
	        // The serial version ID.
			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	            return false; // All cells non-editable
	        }
	    };
		setModel(model);
		
		// Add columns to the model
		model.addColumn("Day");
		model.addColumn("Time");
		model.addColumn("With");
		model.addColumn("Was Attended?");
		model.addColumn("Can be Rescheduled?");
	}
	
	/**
	 * Constructor method. Adds each column and column cell renderers.
	 */
	private void constructColumns() {
		// Generic cell renderer
		TableCellRenderer textRenderer = new TableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        JLabel label = new JLabel((String)value);
		        		        
		        boolean isOdd = ((row % 2) == 1);
		        ColourRegistry text = isSelected ? ColourRegistry.TABLE_SELECTED_TEXT : (isOdd ? ColourRegistry.TABLE_UNSELECTED_1_TEXT : ColourRegistry.TABLE_UNSELECTED_2_TEXT);
		        ColourRegistry background = isSelected ? ColourRegistry.TABLE_SELECTED_BACKGROUND : (isOdd ? ColourRegistry.TABLE_UNSELECTED_1_BACKGROUND : ColourRegistry.TABLE_UNSELECTED_2_BACKGROUND);
		        
		        // Setup label
		        label.setForeground(text.getColour());
		        
		        // Centre
		        JPanel panel = new JPanel(new BorderLayout());
		        panel.setBackground(background.getColour());
		        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		        panel.add(label, BorderLayout.CENTER);
		        
		        // Return
		        return panel;
		    }
		};
		
		// Day of month column
		TableColumn dayOfMonthColumn = this.getColumnModel().getColumn(0);
		dayOfMonthColumn.setHeaderValue("Day");
		dayOfMonthColumn.setCellRenderer(textRenderer);
		
		// Time column
		TableColumn timeColumn = this.getColumnModel().getColumn(1);
		timeColumn.setHeaderValue("Time");
		timeColumn.setCellRenderer(textRenderer);
		
		// Doctor name column
		TableColumn doctorNameColumn = this.getColumnModel().getColumn(2);
		doctorNameColumn.setHeaderValue("With");
		doctorNameColumn.setCellRenderer(textRenderer);
		
		// Was Attended? column with icon renderer
		TableColumn wasAttendedColumn = this.getColumnModel().getColumn(3);
		wasAttendedColumn.setHeaderValue("Was Attended?");
		TableCellRenderer wasAttendedRenderer = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				ImageRegistry icon;
				boolean hasAction = false;
				switch ((WasAttendedEnumUI)value) {
				case YES:
					icon = ImageRegistry.ICON_YES;
					hasAction = true;
					break;
				case NO:
					icon = ImageRegistry.ICON_NO;
					break;
				default:
					icon = ImageRegistry.ICON_NA;
					break;
				}
				// Create label
				JLabel label = new JLabel(icon.getIcon());
				
				// Attach click action
				if (hasAction)
					label.addMouseListener(new MouseAdapter() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			            	int row = getSelectedRow();
			            	if (row < 0)
			            		return;
			            	_parent.attendedAt(row);
			            }
			        });
				
				// Return
				return label;
			}
		};
		wasAttendedColumn.setCellRenderer(wasAttendedRenderer);
		
		// Can be Rescheduled? column with icon renderer
		TableColumn canBeRescheduledColumn = this.getColumnModel().getColumn(4);
		canBeRescheduledColumn.setHeaderValue("Can be Rescheduled?");
		TableCellRenderer canBeRescheduledRenderer = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				ImageRegistry icon;
				icon = (boolean)value ? ImageRegistry.ICON_YES_GO : ImageRegistry.ICON_NO;
				// Create label
				JLabel label = new JLabel(icon.getIcon());
				
				// Attach click action
				if ((boolean)value)
					label.addMouseListener(new MouseAdapter() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			            	int row = getSelectedRow();
			            	if (row < 0)
			            		return;
			            	_parent.rescheduleAt(row);
			            }
			        });
				
				// Return
				return label;
			}
		};
		canBeRescheduledColumn.setCellRenderer(canBeRescheduledRenderer);
	}
	
	/**
	 * Constructor method. Configures the table.
	 */
	public void constructConfiguration() {
		// Selection model
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowSelectionAllowed(true);
		setRowHeight(22);
		setColumnSelectionAllowed(false);
		
		// Detect click
		getSelectionModel().addListSelectionListener(e -> {
		    if (!e.getValueIsAdjusting()) {
		        int selectedRow = getSelectedRow();
		        if (selectedRow >= 0) {
		        	//
		        }
		    }
		});
	}
	
	/**
	 * Constructs an XTableBookings instance.
	 */
	public XTableBookings(XScrollBookings _parent) {
		// Attach parent
		this._parent = _parent;
		
		constructDefaultTableModel();
		constructColumns();
		constructConfiguration();
	}
}