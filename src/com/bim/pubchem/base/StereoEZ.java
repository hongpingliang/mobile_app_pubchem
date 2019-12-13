package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.SpinnerOption;
import com.bim.core.Util;

public class StereoEZ extends SpinnerOption {
	public StereoEZ(String id, String name) {
		super(id, name);
	}
	
	public static String findLabelById(String id) {
		if ( Util.isNull(id)) {
			return null;
		}		
		List<StereoEZ> l = getList();
		for (StereoEZ t : l) {
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
		List<StereoEZ> l = getList();
		int cnt = 0;
		for (StereoEZ t : l) {
			if ( t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}		
	
	public static String find(int position) {
		List<StereoEZ> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}
	
	public String getLabel() {
		return getName();
	}
	public static String[] getLabels() {
		List<StereoEZ> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (StereoEZ t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}
	
	public static List<StereoEZ> list;

	public static List<StereoEZ> getList() {
		if (list != null) {
			return list;
		}
		list = new ArrayList<StereoEZ>();
		list.add(new StereoEZ("", "No limit on E/Z"));
		list.add(new StereoEZ("0[BondChiralCount]", "No E/Z centers"));
		list.add(new StereoEZ("(1[BondChiralCount]:2147483647[BondChiralCount])", "Has E/Z center(s)")); 
		list.add(new StereoEZ("(1[BondChiralCount]:2147483647[BondChiralCount] AND 0[BondChiralDefCount])", "Fully unspecified E/Z centers")); 
		list.add(new StereoEZ("(1[BondChiralUndefCount]:2147483647[BondChiralUndefCount] AND 1[BondChiralDefCount] : 2147483647[BondChiralDefCount])", "Partially specified E/Z centers"));
		list.add(new StereoEZ("(1[BondChiralCount]:2147483647[BondChiralCount] AND 0[BondChiralUndefCount])", "Fully specified E/Z centers"));

		return list;
	}	
}
