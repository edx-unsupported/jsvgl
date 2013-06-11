package edu.umb.jsVGL.client.GeneticModels;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class InteractingGeneModel extends GeneModel {

	private Trait t1;  // recessive trait
	private Trait t2;  // dominant trait

	public InteractingGeneModel(int index) {
		super(index);
	}

	//build from saved work file
	public InteractingGeneModel(
			NodeList traitList, int chromo, int gene) {
		super(gene);

		t1 = TraitFactory.getInstance().buildTrait((Element)traitList.item(0), chromo, gene, 1, true);
		t2 = TraitFactory.getInstance().buildTrait((Element)traitList.item(1), chromo, gene, 2, true);
		setupGenoPhenoTable();
	}

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Trait getRecessiveTrait() {
		return t1;
	}

	public Trait getDominantTrait() {
		return t2;
	}

	public Allele[] getRandomAllelePair(boolean trueBreeding) {
		// want equal frequency of each PHENOTYPE unless true breeding
		Allele[] allelePair = new Allele[2];
		if (rand.nextInt(2) == 0) {
			// recessive pheno (1,1)
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
		} else {
			// dominant pheno - 2 ways to be this
			if ((rand.nextInt(2) == 0) || trueBreeding) {
				// 2,2 homozygote
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t2, 2);				
			} else {
				// 1,2 heterozygote; 2 ways (1,2) and (2,1)
				if(rand.nextInt(2) == 0) {
					allelePair[0] = new Allele(t1, 1);
					allelePair[1] = new Allele(t2, 2);								
				} else {
					allelePair[0] = new Allele(t2, 2);
					allelePair[1] = new Allele(t1, 1);								
				}
			}
		}
		return allelePair;
	}

	public void setupTraits() {
		/* 
		 * there are two alleles and two possible phenos
		 * use A/a and B/b since these are just in 
		 * an interaction
		 */
		if (index == 0) {
			t1 = new SimpleTrait("a");   // recessive
			t2 = new SimpleTrait("A");   // dominant
		} else {
			t1 = new SimpleTrait("b");   // recessive
			t2 = new SimpleTrait("B");   // dominant
		}
	}


	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];

		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2

		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1
		genoPhenoTable[1][2] = new Phenotype(t2);   // 1,2 = 2 (2 is dom)

		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y
		genoPhenoTable[2][1] = new Phenotype(t2);   // 1,2 = 2 (2 is dom)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2
	}

	public String toString() {
		String d;
		String r;
		if (index == 0) {
			d = "A";
			r = "a";
		} else {
			d = "B";
			r = "b";
		}
		StringBuffer b = new StringBuffer();
		b.append("Gene " + d + "<br>");
		b.append("Two Allele Simple Dominance<br>");
		b.append("<ul>");
		b.append("<li>" + d + " is dominant</li>");
		b.append("<li>" + r + " is recessive</li>");
		b.append("</ul>");

		b.append("<table border=1>");
		b.append("<tr><th>Genotype</th><th>Phenotype</th></tr>");
		b.append("<tr><td>" + r + r + "</td>");
		b.append("<td>Non-Functional</td></tr>");

		b.append("<tr><td>" + d + r + "</td>");
		b.append("<td>Functional</td></tr>");

		b.append("<tr><td>" + d + d + "</td>");
		b.append("<td>Functional</td></tr>");

		b.append("</table>");
		return b.toString();
	}
	
	public Element save(int index, float rf) throws Exception {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "Interacting");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.appendChild(t1.save(1));
		e.appendChild(t2.save(2));
		return e;
	}
	
	public String getCharacter() {
		return t1.getBodyPart() + " " + t1.getType();
	}
	
	public Trait[] getTraits() {
		Trait[] t = new Trait[2];
		t[0] = t1;
		t[1] = t2;
		return t;
	}

	public String[] getTraitStrings() {
		String[] t = new String[3];
		t[0] = "?";
		t[1] = t1.getTraitName();
		t[2] = t2.getTraitName();
		return t;
	}

	@Override
	public String getDomTypeText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInteractionHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumAlleles() {
		return -1;
	}


}


