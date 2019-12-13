package com.bim.pubchem.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.SpinnerOption;
import com.bim.core.Util;

public class ChemicalElement extends SpinnerOption {
	public ChemicalElement(String id, String name) {
		super(id, name);
	}
	
	public static int findPositionById(String id) {
		if ( Util.isNull(id)) {
			return -1;
		}		
		List<ChemicalElement> l = getList();
		int cnt = 0;
		for (ChemicalElement t : l) {
			if ( t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}	

	public static String find(int position) {
		List<ChemicalElement> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public String getLabel() {
		if ( Util.isNull(getId())) {
			return getName();
		}
		return getId() + ": " + getName();
	}

	public static String[] getLabels() {
		List<ChemicalElement> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (ChemicalElement t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	public static ChemicalElement findTypeByLabel(String label) {
		for (ChemicalElement t : getList()) {
			if (t.getLabel().equals(label)) {
				return t;
			}
		}
		return null;
	}

	public static List<ChemicalElement> list;

	public static List<ChemicalElement> getList() {
		if (list != null) {
			return list;
		}
		list = new ArrayList<ChemicalElement>();
		list.add(new ChemicalElement("", "No Limit"));

		list.add(new ChemicalElement("Ac", "Actinium"));
		list.add(new ChemicalElement("Ag", "Silver"));
		list.add(new ChemicalElement("Al", "Aluminum"));
		list.add(new ChemicalElement("Am", "Americium"));
		list.add(new ChemicalElement("Ar", "Argon"));
		list.add(new ChemicalElement("As", "Arsenic"));
		list.add(new ChemicalElement("At", "Astatine"));
		list.add(new ChemicalElement("Au", "Gold"));
		list.add(new ChemicalElement("B", "Boron"));
		list.add(new ChemicalElement("Ba", "Barium"));
		list.add(new ChemicalElement("Be", "Beryllium"));
		list.add(new ChemicalElement("Bh", "Bohrium"));
		list.add(new ChemicalElement("Bi", "Bismuth"));
		list.add(new ChemicalElement("Bk", "Berkelium"));
		list.add(new ChemicalElement("Br", "Bromine"));
		list.add(new ChemicalElement("C", "Carbon"));
		list.add(new ChemicalElement("Ca", "Calcium"));
		list.add(new ChemicalElement("Cd", "Cadmium"));
		list.add(new ChemicalElement("Ce", "Cerium"));
		list.add(new ChemicalElement("Cf", "Californium"));
		list.add(new ChemicalElement("Cl", "Chlorine"));
		list.add(new ChemicalElement("Cm", "Curium"));
		list.add(new ChemicalElement("Co", "Cobalt"));
		list.add(new ChemicalElement("Cr", "Chromium"));
		list.add(new ChemicalElement("Cs", "Cesium"));
		list.add(new ChemicalElement("Cu", "Copper"));
		list.add(new ChemicalElement("Db", "Dubnium"));
		list.add(new ChemicalElement("Dy", "Dysprosium"));
		list.add(new ChemicalElement("Er", "Erbium"));
		list.add(new ChemicalElement("Es", "Einsteinium"));
		list.add(new ChemicalElement("Eu", "Europium"));
		list.add(new ChemicalElement("F", "Fluorine"));
		list.add(new ChemicalElement("Fe", "Iron"));
		list.add(new ChemicalElement("Fm", "Fermium"));
		list.add(new ChemicalElement("Fr", "Francium"));
		list.add(new ChemicalElement("Ga", "Gallium"));
		list.add(new ChemicalElement("Gd", "Gadolinium"));
		list.add(new ChemicalElement("Ge", "Germanium"));
		list.add(new ChemicalElement("H", "Hydrogen"));
		list.add(new ChemicalElement("He", "Helium"));
		list.add(new ChemicalElement("Hf", "Hafnium"));
		list.add(new ChemicalElement("Hg", "Mercury"));
		list.add(new ChemicalElement("Ho", "Holmium"));
		list.add(new ChemicalElement("Hs", "Hassium"));
		list.add(new ChemicalElement("I", "Iodine"));
		list.add(new ChemicalElement("In", "Indium"));
		list.add(new ChemicalElement("Ir", "Iridium"));
		list.add(new ChemicalElement("K", "Potassium"));
		list.add(new ChemicalElement("Kr", "Krypton"));
		list.add(new ChemicalElement("La", "Lanthanum"));
		list.add(new ChemicalElement("Li", "Lithium"));
		list.add(new ChemicalElement("Lr", "Lawrencium"));
		list.add(new ChemicalElement("Lu", "Lutetium"));
		list.add(new ChemicalElement("Md", "Mendelevium"));
		list.add(new ChemicalElement("Mg", "Magnesium"));
		list.add(new ChemicalElement("Mn", "Manganese"));
		list.add(new ChemicalElement("Mo", "Molybdenum"));
		list.add(new ChemicalElement("Mt", "Meitnerium"));
		list.add(new ChemicalElement("N", "Nitrogen"));
		list.add(new ChemicalElement("Na", "Sodium"));
		list.add(new ChemicalElement("Nb", "Niobium"));
		list.add(new ChemicalElement("Nd", "Neodymium"));
		list.add(new ChemicalElement("Ne", "Neon"));
		list.add(new ChemicalElement("Ni", "Nickel"));
		list.add(new ChemicalElement("No", "Nobelium"));
		list.add(new ChemicalElement("Np", "Neptunium"));
		list.add(new ChemicalElement("O", "Oxygen"));
		list.add(new ChemicalElement("Os", "Osmium"));
		list.add(new ChemicalElement("P", "Phosphorus"));
		list.add(new ChemicalElement("Pa", "Protactinium"));
		list.add(new ChemicalElement("Pb", "Lead"));
		list.add(new ChemicalElement("Pd", "Palladium"));
		list.add(new ChemicalElement("Pm", "Promethium"));
		list.add(new ChemicalElement("Po", "Polonium"));
		list.add(new ChemicalElement("Pr", "Praseodymium"));
		list.add(new ChemicalElement("Pt", "Platinum"));
		list.add(new ChemicalElement("Pu", "Plutonium"));
		list.add(new ChemicalElement("Ra", "Radium"));
		list.add(new ChemicalElement("Rb", "Rubidium"));
		list.add(new ChemicalElement("Re", "Rhenium"));
		list.add(new ChemicalElement("Rf", "Rutherfordium"));
		list.add(new ChemicalElement("Rh", "Rhodium"));
		list.add(new ChemicalElement("Rn", "Radon"));
		list.add(new ChemicalElement("Ru", "Ruthenium"));
		list.add(new ChemicalElement("S", "Sulfur"));
		list.add(new ChemicalElement("Sb", "Antimony"));
		list.add(new ChemicalElement("Sc", "Scandium"));
		list.add(new ChemicalElement("Se", "Selenium"));
		list.add(new ChemicalElement("Sg", "Seaborgium"));
		list.add(new ChemicalElement("Si", "Silicon"));
		list.add(new ChemicalElement("Sm", "Samarium"));
		list.add(new ChemicalElement("Sn", "Tin"));
		list.add(new ChemicalElement("Sr", "Strontium"));
		list.add(new ChemicalElement("Ta", "Tantalum"));
		list.add(new ChemicalElement("Tb", "Terbium"));
		list.add(new ChemicalElement("Tc", "Technetium"));
		list.add(new ChemicalElement("Te", "Tellurium"));
		list.add(new ChemicalElement("Th", "Thorium"));
		list.add(new ChemicalElement("Ti", "Titanium"));
		list.add(new ChemicalElement("Tl", "Thallium"));
		list.add(new ChemicalElement("Tm", "Thulium"));
		list.add(new ChemicalElement("U", "Uranium"));
		list.add(new ChemicalElement("V", "Vanadium"));
		list.add(new ChemicalElement("W", "Tungsten"));
		list.add(new ChemicalElement("Xe", "Xenon"));
		list.add(new ChemicalElement("Y", "Yttrium"));
		list.add(new ChemicalElement("Yb", "Ytterbium"));
		list.add(new ChemicalElement("Zn", "Zinc"));
		list.add(new ChemicalElement("Zr", "Zirconium"));

		return list;
	}
}
