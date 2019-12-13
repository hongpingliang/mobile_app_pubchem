package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.SpinnerOption;
import com.bim.core.Util;

public class StereoChirality extends SpinnerOption {
	public StereoChirality(String id, String name) {
		super(id, name);
	}
	
	public static String findLabelById(String id) {
		if ( Util.isNull(id)) {
			return null;
		}		
		List<StereoChirality> l = getList();
		for (StereoChirality t : l) {
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
		List<StereoChirality> l = getList();
		int cnt = 0;
		for (StereoChirality t : l) {
			if ( t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}	
	
	public static String find(int position) {
		List<StereoChirality> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public String getLabel() {
		return getName();
	}

	public static String[] getLabels() {
		List<StereoChirality> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (StereoChirality t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	public static List<StereoChirality> list;

	public static List<StereoChirality> getList() {
		if (list != null) {
			return list;
		}
		list = new ArrayList<StereoChirality>();
		list.add(new StereoChirality("", "No limit on chirality"));
		list.add(new StereoChirality("0[AtomChiralCount]", "No chiral centers"));
		list.add(new StereoChirality("(1[AtomChiralCount]:2147483647[AtomChiralCount])", "Has chiral center(s)"));
		list.add(new StereoChirality("(1[AtomChiralCount] : 2147483647[AtomChiralCount] AND 0[AtomChiralDefCount])", "Fully unspecified chiral centers")); 
		list.add(new StereoChirality("(1[AtomChiralUndefCount] : 2147483647[AtomChiralUndefCount] AND 1[AtomChiralDefCount] : 2147483647[AtomChiralDefCount])", "Partially specified chiral centers"));
		list.add(new StereoChirality("(1[AtomChiralCount] : 2147483647[AtomChiralCount] AND 0[AtomChiralUndefCount])", "Fully specified chiral centers"));

		return list;
	}
}
