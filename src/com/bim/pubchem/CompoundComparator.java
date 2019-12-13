package com.bim.pubchem;

import java.util.Comparator;

import com.bim.core.Util;
import com.bim.pubchem.base.ECompound;

public class CompoundComparator implements Comparator {

	private int byResId;
	private boolean isDescOrder;

	public CompoundComparator(int byResId, boolean isDescOrder) {
		this.byResId = byResId;
		this.isDescOrder = isDescOrder;
	}

	public int compare(Object object1, Object object2) {
		ECompound a = (ECompound) object1;
		ECompound b = (ECompound) object2;

		String aStr = null;
		String bStr = null;
		switch (byResId) {
		case R.id.sort_by_cid:
			if (isDescOrder) {
				return b.getCid() - a.getCid();
			} else {
				return a.getCid() - b.getCid();
			}
		case R.id.sort_by_date_created:
			aStr = a.getCreateDate();
			bStr = b.getCreateDate();
			break;
		case R.id.sort_by_iupac:
			aStr = a.getIupac();
			bStr = b.getIupac();
			break;
		case R.id.sort_by_mol_weight:
			if (isDescOrder) {
				return (int) (Util.toDouble(b.getMolWeight()) - Util.toDouble(a
						.getMolWeight()));
			} else {
				return (int) (Util.toDouble(a.getMolWeight()) - Util.toDouble(b
						.getMolWeight()));
			}
		default:
			break;
		}

		if (isDescOrder) {
			String t = aStr;
			aStr = bStr;
			bStr = t;
		}

		return byName(aStr, bStr);
	}

	private int byName(String a, String b) {
		if (Util.isNull(a) && Util.isNull(b)) {
			return 0;
		} else if (Util.isNull(a)) {
			return -1;
		} else if (Util.isNull(b)) {
			return 1;
		} else {
			return a.compareToIgnoreCase(b);
		}
	}
}