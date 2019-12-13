package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bim.core.EParceble;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;

public class ECompound extends EParceble {
	private final static String IMG_SRC = "https://pubchem.ncbi.nlm.nih.gov/image/imgsrv.fcgi?cid=";

	private int id;
	private int cid;
	private String createDate;
	private List<String> synonymList;
	private String iupac;
	private String molWeight;
	private String formula;
	private int totalAssay;
	private int activeAssay;
	private List<String> pharmActionList;

	private boolean checked;
	private Bitmap iconBitmap;
	private boolean isLoadingIcon;

	public ECompound() {
		synonymList = new ArrayList<String>();
		pharmActionList = new ArrayList<String>();
	}

	public String toJsonString() {
		JSONStringer json = new JSONStringer();
		try {
			json.object();
			json.key("id").value(getId());
			json.key("cid").value(getCid());
			set(json, "createDate", getCreateDate());
			set(json, "iupac", getIupac());
			set(json, "molWeight", getMolWeight());
			set(json, "formula", getFormula());
			set(json, "totalAssay", getTotalAssay());
			set(json, "activeAssay", getActiveAssay());
			
			if (synonymList != null && synonymList.size() > 0) {
				json.key("synonymList");
				json.array();
				
				for (String a : synonymList) {
					if (Util.isNull(a)) {
						continue;
					}
					JSONObject item = new JSONObject();
					item.put("s", a);
					json.value(item);
				}
				json.endArray();
			}
			
			if (pharmActionList != null && pharmActionList.size() > 0) {
				json.key("pharmActionList");
				json.array();
				
				for (String a : pharmActionList) {
					if (Util.isNull(a)) {
						continue;
					}
					JSONObject item = new JSONObject();
					item.put("p", a);
					json.value(item);
				}
				json.endArray();
			}
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
			JSONObject json = new JSONObject(jsonStr);
			id = json.getInt("id");
			cid =  getInt(json, "cid"); 
			createDate = getString(json, "createDate");
			iupac = getString(json, "iupac");
			molWeight = getString(json, "molWeight");
			formula = getString(json, "formula");
			totalAssay = getInt(json, "totalAssay");
			activeAssay = getInt(json, "activeAssay");

			if (json.has("synonymList")) {
				JSONArray array = json.getJSONArray("synonymList");
				if (array != null && array.length() > 0) {
					synonymList = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject arrayJson = array.getJSONObject(i);
						String a = getString(arrayJson, "s");
						if ( !Util.isNull(a)) {
							synonymList.add(a);
						}
					}
				}
			}

			if (json.has("pharmActionList")) {
				JSONArray array = json.getJSONArray("pharmActionList");
				if (array != null && array.length() > 0) {
					pharmActionList = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject arrayJson = array.getJSONObject(i);
						String a = getString(arrayJson, "p");
						if ( !Util.isNull(a)) {
							pharmActionList.add(a);
						}
					}
				}
			}			
		} catch (JSONException e) {
			Log.d(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
	public void updateOrLoadImage(ActivityPub activity, ImageView mImageView) {
		if (iconBitmap != null) {
			mImageView.setImageBitmap(iconBitmap);
			return;
		}
		if (isLoadingIcon) {
			return;
		}
		isLoadingIcon = true;

		String src = getImageSrc();
		if (Log.IS_DEBUG) {
			src = "http://www.bioinfomobile.com/temp/chem_9581.png";
		}
//		ImageLoader loader = new ImageLoader(activity, src, this);
//		loader.start();
	}

	private ECompound(Parcel in) {
		id = in.readInt();
		cid = in.readInt();
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeInt(cid);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ECompound createFromParcel(Parcel in) {
			return new ECompound(in);
		}

		public ECompound[] newArray(int size) {
			return new ECompound[size];
		}
	};

	public String getPharmAction() {
		int MAX = 2;
		if (pharmActionList == null) {
			return null;
		}
		String t = "";
		int cnt = 1;
		for (String s : pharmActionList) {
			if (cnt > 1) {
				t += "; ";
			}
			t += s;
			cnt++;
			if (cnt > MAX) {
				break;
			}
		}

		if (pharmActionList.size() > MAX) {
			t += "...";
		}
		return t;
	}

	public String getSynonym() {
		int MAX = 3;
		if (synonymList == null) {
			return null;
		}
		String t = "";
		int cnt = 1;
		for (String s : synonymList) {
			if (cnt > 1) {
				t += "; ";
			}
			t += s;
			cnt++;
			if (cnt > MAX) {
				break;
			}
		}

		if (synonymList.size() > MAX) {
			t += "...";
		}
		return t;
	}

	public String getImageSrc() {
		return IMG_SRC + getCid();
	}

	public boolean isDataOkay() {
		if (getCid() <= 1) {
			return false;
		}
		return true;
	}

	public String toString() {
		return getCid() + " " + getIupac() + " " + getFormula();
	}

	
	
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public List<String> getSynonymList() {
		return synonymList;
	}

	public void setSynonymList(List<String> synonymList) {
		this.synonymList = synonymList;
	}

	public String getIupac() {
		return iupac;
	}

	public void setIupac(String iupac) {
		this.iupac = iupac;
	}

	public String getMolWeight() {
		return molWeight;
	}

	public void setMolWeight(String molWeight) {
		this.molWeight = molWeight;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public int getTotalAssay() {
		return totalAssay;
	}

	public void setTotalAssay(int totalAssay) {
		this.totalAssay = totalAssay;
	}

	public int getActiveAssay() {
		return activeAssay;
	}

	public void setActiveAssay(int activeAssay) {
		this.activeAssay = activeAssay;
	}

	public List<String> getPharmActionList() {
		return pharmActionList;
	}

	public void setPharmActionList(List<String> pharmActionList) {
		this.pharmActionList = pharmActionList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Bitmap getIconBitmap() {
		return iconBitmap;
	}

	public void setIconBitmap(Bitmap iconBitmap) {
		this.iconBitmap = iconBitmap;
	}
}
