package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.SpinnerOption;
import com.bim.core.Util;

public class BioAssay extends SpinnerOption {
	public BioAssay(String id, String name) {
		super(id, name);
	}

	public static String findLabelById(String id) {
		if ( Util.isNull(id)) {
			return null;
		}
		List<BioAssay> l = getList();
		for (BioAssay t : l) {
			if ( t.getId().equals(id)) {
				return t.getLabel();
			}
		}
		return null;
	}	
	public static int findPositionById(String id) {
		if ( Util.isNull(id)) {
			return -1;
		}		
		List<BioAssay> l = getList();
		int cnt = 0;
		for (BioAssay t : l) {
			if ( t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}		
	
	public static String find(int position) {
		List<BioAssay> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public String getLabel() {
		return getName();
	}

	public static String[] getLabels() {
		List<BioAssay> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (BioAssay t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	public static List<BioAssay> list;

	public static List<BioAssay> getList() {
		if (list != null) {
			return list;
		}
		list = new ArrayList<BioAssay>();
		list.add(new BioAssay("", "No Limit"));
		list.add(new BioAssay("pccompound_pcassay[Filter]", "Tested"));
		list.add(new BioAssay("pccompound_pcassay_probe[Filter]", "Probe"));
		list.add(new BioAssay("pccompound_pcassay_active[Filter]", "Active"));
		list.add(new BioAssay("pccompound_pcassay_inactive[Filter] ",
				"Inactive"));
		return list;
	}
}