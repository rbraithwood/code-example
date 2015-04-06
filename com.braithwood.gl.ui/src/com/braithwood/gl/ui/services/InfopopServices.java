package com.braithwood.gl.ui.services;

import java.net.URL;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.contributions.Infopop;
import com.braithwood.gl.ui.contributions.InfopopEntry;

public class InfopopServices {

	private static InfopopServices INSTANCE;

	private SortedMap<String, InfopopEntry> entries;

	// singleton
	private InfopopServices() {
		super();

		populateEntries();
	}

	public static InfopopServices getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InfopopServices();
		}
		return INSTANCE;
	}

	public InfopopEntry getEntry(String id) {
		return entries.get(id);
	}

	private void populateEntries() {
		entries = new TreeMap<String, InfopopEntry>();

		URL fileLocation = GameLibraryUIActivator.getDefault().getBundle().getResource("infopop/infopop.xml");

		Infopop infopop;
		try {
			JAXBContext context = JAXBContext.newInstance(Infopop.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			infopop = (Infopop) unmarshaller.unmarshal(fileLocation);
		} catch (JAXBException e) {
			infopop = null;
		}

		if (infopop != null) {
			for (InfopopEntry next : infopop.getEntries()) {
				entries.put(next.getId(), next);
			}
		}
	}
}