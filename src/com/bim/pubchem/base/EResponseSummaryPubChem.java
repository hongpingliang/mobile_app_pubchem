package com.bim.pubchem.base;

import java.util.ArrayList;

import com.bim.ncbi.EResponse;


import android.os.Parcel;
import android.os.Parcelable;

public class EResponseSummaryPubChem extends EResponse {

	private ArrayList<ECompound> compoundList;

	public EResponseSummaryPubChem() {
		compoundList = new ArrayList<ECompound>();
	}
	
	private EResponseSummaryPubChem(Parcel in) {
	}
	
	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public EResponseSummaryPubChem createFromParcel(Parcel in) {
			return new EResponseSummaryPubChem(in);
		}

		public EResponseSummaryPubChem[] newArray(int size) {
			return new EResponseSummaryPubChem[size];
		}
	};

	public ArrayList<ECompound> getCompoundList() {
		return compoundList;
	}
}
