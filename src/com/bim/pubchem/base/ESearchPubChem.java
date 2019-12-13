package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.os.Parcel;
import android.os.Parcelable;

import com.bim.core.Util;
import com.bim.ncbi.ESearch;

public class ESearchPubChem extends ESearch {
	public static final String SEPARATOR = ":";
	private String fromDate;
	private String toDate;
	private String stereoChirality;
	private String stereoEZ;
	private String bioAssay;
	private List<String> elementList = new ArrayList<String>();

	private String MolecularWeight;
	private String XLogP;
	private String HydrogenBondDonorCount;
	private String HydrogenBondAcceptorCount;
	private String RotatableBondCount;
	private String TPSA;
	private String HeavyAtomCount;
	private String IsotopeAtomCount;
	private String TautomerCount;
	private String CovalentUnitCount;
	private String Complexity;
	private String TotalFormalCharge;

	public String getCreateDateLabel() {
		if ( Util.isNull(fromDate)) {
			return toDate;
		}
		if ( Util.isNull(toDate)) {
			return fromDate;
		}
		return 	fromDate + " - " + toDate;	
	}
	
	public List<String> getChemicalPropertyList() {
		List<String> list = new ArrayList<String>();
		
		list.add(get("MolecularWeight", getMolecularWeight()));
		list.add(get("XLogP", getXLogP()));
		list.add(get("HydrogenBondDonorCount", getHydrogenBondDonorCount()));
		list.add(get("HydrogenBondAcceptorCount", getHydrogenBondAcceptorCount()));
		list.add(get("RotatableBondCount", getRotatableBondCount()));
		list.add(get("TPSA", getTPSA()));
		list.add(get("HeavyAtomCount", getHeavyAtomCount()));
		list.add(get("IsotopeAtomCount", getIsotopeAtomCount()));
		list.add(get("TautomerCount", getTautomerCount()));
		list.add(get("CovalentUnitCount", getCovalentUnitCount()));
		list.add(get("Complexity", getComplexity()));
		list.add(get("TotalFormalCharge", getTotalFormalCharge()));
		
		return list;
	}

	private String get(String name, String value) {
		if ( Util.isNull(value)) {
			return null;
		}
		
		String[] pairs = value.split(ESearchPubChem.SEPARATOR);
		if (pairs == null) {
			return null;
		}
		int len = pairs.length;
		String t  = "";
		if (len > 0 && !Util.isNull(pairs[0])) {
			t += pairs[0];
		}
		
		if (len > 1 && !Util.isNull(pairs[1])) {
			if ( !Util.isNull(t)) {
				t += " - ";
			}
			t += pairs[1];
		}
		if ( !Util.isNull(t)) {
			return name + ": " + t;
		}
		return null;
	}

	public ESearchPubChem() {
		super();
	}

	public String getStereoChiralityLabel() {
		if (Util.isNull(stereoChirality)) {
			return null;
		}
		return StereoChirality.findLabelById(stereoChirality);
	}

	public String getStereoEZLabel() {
		if (Util.isNull(stereoEZ)) {
			return null;
		}
		return StereoEZ.findLabelById(stereoEZ);
	}

	public String getBioAssayLabel() {
		if (Util.isNull(bioAssay)) {
			return null;
		}
		return BioAssay.findLabelById(bioAssay);
	}

	public String getElementsLabel() {
		if (elementList == null || elementList.size() < 1) {
			return null;
		}
		String t = "";
		int cnt = 0;
		for (String s : elementList) {
			if (cnt > 0) {
				t += ", ";
			}
			t += s;
			cnt++;
		}
		return t;
	}

	private ESearchPubChem(Parcel in) {
		setTime(in.readString());
		setTerm(in.readString());

		setFromDate(in.readString());
		setToDate(in.readString());
		setStereoChirality(in.readString());
		setStereoEZ(in.readString());
		setBioAssay(in.readString());
		in.readStringList(elementList);

		setMolecularWeight(in.readString());
		setXLogP(in.readString());
		setHydrogenBondDonorCount(in.readString());
		setHydrogenBondAcceptorCount(in.readString());
		setRotatableBondCount(in.readString());
		setTPSA(in.readString());
		setHeavyAtomCount(in.readString());
		setIsotopeAtomCount(in.readString());
		setTautomerCount(in.readString());
		setCovalentUnitCount(in.readString());
		setComplexity(in.readString());
		setTotalFormalCharge(in.readString());

		setResult(in.readInt());
		setSort(in.readString());
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(getTime());
		out.writeString(getTerm());
		out.writeString(getFromDate());
		out.writeString(getToDate());
		out.writeString(getStereoChirality());
		out.writeString(getStereoEZ());
		out.writeString(getBioAssay());
		out.writeStringList(elementList);

		out.writeString(getMolecularWeight());
		out.writeString(getXLogP());
		out.writeString(getHydrogenBondDonorCount());
		out.writeString(getHydrogenBondAcceptorCount());
		out.writeString(getRotatableBondCount());
		out.writeString(getTPSA());
		out.writeString(getHeavyAtomCount());
		out.writeString(getIsotopeAtomCount());
		out.writeString(getTautomerCount());
		out.writeString(getCovalentUnitCount());
		out.writeString(getComplexity());
		out.writeString(getTotalFormalCharge());

		out.writeInt(getResult());
		out.writeString(getSort());
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ESearchPubChem createFromParcel(Parcel in) {
			return new ESearchPubChem(in);
		}

		public ESearchPubChem[] newArray(int size) {
			return new ESearchPubChem[size];
		}
	};

	public String toJsonString() {
		JSONStringer json = new JSONStringer();
		try {
			json.object();
			json.key("time").value(getTime());
			set(json, "term", getTerm());
			set(json, "fromDate", getFromDate());
			set(json, "toDate", getToDate());
			set(json, "stereoChirality", getStereoChirality());
			set(json, "stereoEZ", getStereoEZ());
			set(json, "bioAssay", getBioAssay());

			if (elementList != null && elementList.size() > 0) {
				json.key("elementList");
				json.array();

				for (String a : elementList) {
					if (Util.isNull(a)) {
						continue;
					}
					JSONObject item = new JSONObject();
					item.put("element", a);
					json.value(item);
				}
				json.endArray();
			}

			set(json, "MolecularWeight", getMolecularWeight());
			set(json, "XLogP", getXLogP());
			set(json, "HydrogenBondDonorCount", getHydrogenBondDonorCount());
			set(json, "HydrogenBondAcceptorCount",
					getHydrogenBondAcceptorCount());
			set(json, "RotatableBondCount", getRotatableBondCount());
			set(json, "TPSA", getTPSA());
			set(json, "HeavyAtomCount", getHeavyAtomCount());
			set(json, "IsotopeAtomCount", getIsotopeAtomCount());
			set(json, "TautomerCount", getTautomerCount());
			set(json, "CovalentUnitCount", getCovalentUnitCount());
			set(json, "Complexity", getComplexity());
			set(json, "TotalFormalCharge", getTotalFormalCharge());

			set(json, "result", getResult());
			set(json, "sort", getSort());

			json.endObject();
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean parse(String jsonStr) {
		if (Util.isNull(jsonStr)) {
			return false;
		}

		try {
			JSONObject json = new JSONObject(jsonStr.trim());
			setTime(json.getString("time"));
			setTerm(getString(json, "term"));
			setFromDate(getString(json, "fromDate"));
			setToDate(getString(json, "toDate"));

			setStereoChirality(getString(json, "stereoChirality"));
			setStereoEZ(getString(json, "stereoEZ"));
			setBioAssay(getString(json, "bioAssay"));

			if (json.has("elementList")) {
				JSONArray array = json.getJSONArray("elementList");
				if (array != null && array.length() > 0) {
					elementList = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject arrayJson = array.getJSONObject(i);
						String a = getString(arrayJson, "element");
						if (!Util.isNull(a)) {
							elementList.add(a);
						}
					}
				}
			}
			setMolecularWeight(getString(json, "MolecularWeight"));
			setXLogP(getString(json, "XLogP"));
			setHydrogenBondDonorCount(getString(json, "HydrogenBondDonorCount"));
			setHydrogenBondAcceptorCount(getString(json,
					"HydrogenBondAcceptorCount"));
			setRotatableBondCount(getString(json, "RotatableBondCount"));
			setTPSA(getString(json, "TPSA"));
			setHeavyAtomCount(getString(json, "HeavyAtomCount"));
			setIsotopeAtomCount(getString(json, "IsotopeAtomCount"));
			setTautomerCount(getString(json, "TautomerCount"));
			setCovalentUnitCount(getString(json, "CovalentUnitCount"));
			setComplexity(getString(json, "Complexity"));
			setTotalFormalCharge(getString(json, "TotalFormalCharge"));

			setResult(getInt(json, "result"));
			setSort(getString(json, "sort"));

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getMolecularWeight() {
		return MolecularWeight;
	}

	public void setMolecularWeight(String molecularWeight) {
		MolecularWeight = molecularWeight;
	}

	public String getXLogP() {
		return XLogP;
	}

	public void setXLogP(String logP) {
		XLogP = logP;
	}

	public String getHydrogenBondDonorCount() {
		return HydrogenBondDonorCount;
	}

	public void setHydrogenBondDonorCount(String hydrogenBondDonorCount) {
		HydrogenBondDonorCount = hydrogenBondDonorCount;
	}

	public String getHydrogenBondAcceptorCount() {
		return HydrogenBondAcceptorCount;
	}

	public void setHydrogenBondAcceptorCount(String hydrogenBondAcceptorCount) {
		HydrogenBondAcceptorCount = hydrogenBondAcceptorCount;
	}

	public String getRotatableBondCount() {
		return RotatableBondCount;
	}

	public void setRotatableBondCount(String rotatableBondCount) {
		RotatableBondCount = rotatableBondCount;
	}

	public String getTPSA() {
		return TPSA;
	}

	public void setTPSA(String tpsa) {
		TPSA = tpsa;
	}

	public String getHeavyAtomCount() {
		return HeavyAtomCount;
	}

	public void setHeavyAtomCount(String heavyAtomCount) {
		HeavyAtomCount = heavyAtomCount;
	}

	public String getIsotopeAtomCount() {
		return IsotopeAtomCount;
	}

	public void setIsotopeAtomCount(String isotopeAtomCount) {
		IsotopeAtomCount = isotopeAtomCount;
	}

	public String getTautomerCount() {
		return TautomerCount;
	}

	public void setTautomerCount(String tautomerCount) {
		TautomerCount = tautomerCount;
	}

	public String getCovalentUnitCount() {
		return CovalentUnitCount;
	}

	public void setCovalentUnitCount(String covalentUnitCount) {
		CovalentUnitCount = covalentUnitCount;
	}

	public String getComplexity() {
		return Complexity;
	}

	public void setComplexity(String complexity) {
		Complexity = complexity;
	}

	public String getTotalFormalCharge() {
		return TotalFormalCharge;
	}

	public void setTotalFormalCharge(String totalFormalCharge) {
		TotalFormalCharge = totalFormalCharge;
	}

	public String getStereoChirality() {
		return stereoChirality;
	}

	public void setStereoChirality(String stereoChirality) {
		this.stereoChirality = stereoChirality;
	}

	public String getStereoEZ() {
		return stereoEZ;
	}

	public void setStereoEZ(String stereoEZ) {
		this.stereoEZ = stereoEZ;
	}

	public String getBioAssay() {
		return bioAssay;
	}

	public void setBioAssay(String bioAssay) {
		this.bioAssay = bioAssay;
	}

	public List<String> getElementList() {
		return elementList;
	}

	public void setElementList(List<String> elementList) {
		this.elementList = elementList;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
