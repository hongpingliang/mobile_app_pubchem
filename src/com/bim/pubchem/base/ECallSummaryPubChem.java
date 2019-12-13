package com.bim.pubchem.base;

import org.xml.sax.SAXException;

import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSummary;

public class ECallSummaryPubChem extends ECallSummary {

	private static final String TAG_CID = "CID";
	private static final String TAG_SynonymList = "SynonymList";
	private static final String TAG_CreateDate = "CreateDate";
	private static final String TAG_PharmActionList = "PharmActionList";
	private static final String TAG_IUPACName = "IUPACName";
	private static final String TAG_MolecularFormula = "MolecularFormula";
	private static final String TAG_MolecularWeight = "MolecularWeight";
	private static final String TAG_TotalAidCount = "TotalAidCount";
	private static final String TAG_ActiveAidCount = "ActiveAidCount";

	private static final int LEVEL_DocSum = 2;
	private static final int LEVEL_MAIN = 3;
	private static final int LEVEL_MAIN_UNDER = 4;

	private int level = 0;

	private EResponseSummaryPubChem response;

	private ECompound compound;

	private boolean isCatchValue = false;
	private String value;

	private boolean isIdStarted;
	private boolean isCIdStarted;
	private boolean isCreateDateStarted;
	private boolean isSynonymListStarted;
	private boolean isPharmActionListStarted;
	private boolean isIUPACNameStarted;
	private boolean isMolecularFormulaStarted;
	private boolean isMolecularWeightStarted;
	private boolean isTotalAidCountStarted;
	private boolean isActiveAidCount;

	public ECallSummaryPubChem(ActivityPub activityBase) {
		super(activityBase);
		response = new EResponseSummaryPubChem();
	}

	public void startElement(String uri, String localName, String name,
			org.xml.sax.Attributes attributes) throws SAXException {
		level++;

		// Log.d("begin " + localName + " " + level);

		isCatchValue = false;
		value = null;
		if (level == LEVEL_DocSum && TAG_DocSum.equals(localName)) {
			compound = new ECompound();
			return;
		}

		if (level == LEVEL_MAIN) {
			if (TAG_Id.equals(localName)) {
				isCatchValue = true;
				isIdStarted = true;
				return;
			}

			if (attributes == null) {
				return;
			}
			if (!TAG_Item.equals(localName)) {
				return;
			}

			String attValue = attributes.getValue(TAG_ATT_NAME);
			if (TAG_SynonymList.equals(attValue)) {
				isSynonymListStarted = true;
				isCatchValue = false;
				return;
			}

			if (TAG_PharmActionList.equals(attValue)) {
				isPharmActionListStarted = true;
				isCatchValue = false;
				return;
			}

			if (TAG_CID.equals(attValue)) {
				isCIdStarted = true;
				isCatchValue = true;
			} else if (TAG_CreateDate.equals(attValue)) {
				isCreateDateStarted = true;
				isCatchValue = true;
			} else if (TAG_IUPACName.equals(attValue)) {
				isIUPACNameStarted = true;
				isCatchValue = true;
			} else if (TAG_MolecularFormula.equals(attValue)) {
				isMolecularFormulaStarted = true;
				isCatchValue = true;
			} else if (TAG_MolecularWeight.equals(attValue)) {
				isMolecularWeightStarted = true;
				isCatchValue = true;
			} else if (TAG_TotalAidCount.equals(attValue)) {
				isTotalAidCountStarted = true;
				isCatchValue = true;
			} else if (TAG_ActiveAidCount.equals(attValue)) {
				isActiveAidCount = true;
				isCatchValue = true;
			}
			return;
		}

		if (level == LEVEL_MAIN_UNDER) {
			if (!TAG_Item.equals(localName)) {
				return;
			}
			if (isSynonymListStarted || isPharmActionListStarted) {
				isCatchValue = true;
				return;
			}
		}
	}

	public void characters(char ch[], int start, int length) {
		if (compound == null || !isCatchValue) {
			return;
		}
		value = getString(ch, start, length);
		// Log.d(value);
	}

	public void endElement(String namespaceURI, String localName, String qName) {
		doEndElement(namespaceURI, localName, qName);
		level--;
	}

	private void doEndElement(String namespaceURI, String localName,
			String qName) {
		// Log.d("end " + localName + " " + level);
		isCatchValue = false;
		if (compound == null) {
			return;
		}

		if (level == LEVEL_DocSum && TAG_DocSum.equals(localName)) {
			if (compound.isDataOkay()) {
				response.getCompoundList().add(compound);
			}
			compound = null;
			return;
		}

		if (level == LEVEL_MAIN) {
			if (isIdStarted) {
				compound.setId(Util.toInt(value));
				isIdStarted = false;
				return;
			}
			if (isCIdStarted) {
				compound.setCid(Util.toInt(value));
				isCIdStarted = false;
			} else if (isCreateDateStarted) {
				if ( value != null ) {
					value = value.replace(" 00:00", "");
				}
				compound.setCreateDate(value);
				isCreateDateStarted = false;
			} else if (isIUPACNameStarted) {
				compound.setIupac(value);
				isIUPACNameStarted = false;
			} else if (isMolecularWeightStarted) {
				compound.setMolWeight(value);
				isMolecularWeightStarted = false;
			} else if (isMolecularFormulaStarted) {
				compound.setFormula(value);
				isMolecularFormulaStarted = false;
			} else if (isTotalAidCountStarted) {
				compound.setTotalAssay(Util.toInt(value));
				isTotalAidCountStarted = false;
			} else if (isActiveAidCount) {
				compound.setActiveAssay(Util.toInt(value));
				isActiveAidCount = false;
			} else if (isSynonymListStarted) {
				isSynonymListStarted = false;
			} else if (isPharmActionListStarted) {
				isPharmActionListStarted = false;
			}
			return;
		}
		if (level == LEVEL_MAIN_UNDER) {
			if (isSynonymListStarted) {
				compound.getSynonymList().add(value);
				if ( compound.getSynonymList().size() > 3) {
					isSynonymListStarted = false;
				}
			} else if (isPharmActionListStarted) {
				compound.getPharmActionList().add(value);
				if ( compound.getPharmActionList().size() > 3) {
					isPharmActionListStarted = false;
				}
			}
		}
	}

	public EResponseSummaryPubChem getResponse() {
		return response;
	}
}
