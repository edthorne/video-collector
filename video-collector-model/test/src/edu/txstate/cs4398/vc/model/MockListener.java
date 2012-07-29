package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock listener class for use during test execution.
 * 
 * @author Ed
 */
public class MockListener implements ModelListener {
	private List<ModelEvent> events = new ArrayList<ModelEvent>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.txstate.cs4398.vc.model.ModelListener#modelChanged(edu.txstate.cs4398.vc.model.ModelEvent)
	 */
	@Override
	public void modelChanged(ModelEvent event) {
		events.add(event);
	}

	public boolean containsEvent(Object source, int id) {
		for (ModelEvent event : events) {
			if (event.getSource() == source && event.getID() == id) {
				return true;
			}
		}
		return false;
	}

	public int countEvents(Object source, int id) {
		int count = 0;
		for (ModelEvent event : events) {
			if (event.getSource() == source && event.getID() == id) {
				count++;
			}
		}
		return count;
	}

	public void reset() {
		events.clear();
	}

}
