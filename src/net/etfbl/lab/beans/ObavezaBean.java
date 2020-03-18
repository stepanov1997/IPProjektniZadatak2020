package net.etfbl.lab.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import net.etfbl.lab.dao.ObavezeDAO;
import net.etfbl.lab.dto.Obaveza;
import net.etfbl.lab.util.data.Category;
import net.etfbl.lab.util.data.Picture;

@ManagedBean(name = "obavezaBean")
@SessionScoped
public class ObavezaBean implements Serializable {

	public ObavezaBean() {
		init();
	} 

	private static final long serialVersionUID = -8606687324815905107L;
	private ArrayList<Obaveza> obaveze = new ArrayList<Obaveza>();
	private Obaveza obaveza = new Obaveza();
	private String datum = new String();
	private String poruka;
	private String name;
	private List<Category> categories = new ArrayList<>();

	public ArrayList<Obaveza> getObaveze() {
		return obaveze;
	}

	public void setObaveze(ArrayList<Obaveza> obaveze) {
		this.obaveze = obaveze;
	}

	public Obaveza getObaveza() {
		return obaveza;
	}

	public void setObaveza(Obaveza obaveza) {
		this.obaveza = obaveza;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public String ocitajObavezeIzBaze() {
		obaveze = ObavezeDAO.ocitajObavezePoDatumu(datum);
		return "obaveze.xhtml?faces-redirect=true";
	}

	public String upisiObavezu() {
		boolean uspjeh = ObavezeDAO.unesiObavezu(obaveza);
		if (uspjeh) {
			this.obaveza = new Obaveza();
			this.poruka = "Unos u bazu je uspjesno obavljen!";
		}
		return null;
	}

	public String obrisiObavezu() {
		Map<String, String> reqMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (reqMap.containsKey("id")) {
			int z = Integer.parseInt(reqMap.get("id"));
			for (Obaveza o : obaveze) {
				if (o.getId() == z) {
					obaveze.remove(o);
					ObavezeDAO.obrisiObavezu(z);
					break;
				}
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWelcomeMessage() {
		return "Ajax poruka " + name;
	}

	public void onChangeInput(AjaxBehaviorEvent event) {
		String value = (String) ((UIOutput) event.getSource()).getValue();
		this.name = value;
	}

	// podaci za tabelu
	private void init() {
		categories = new ArrayList<>();

		Picture pic11 = new Picture();
		pic11.setId("11");
		pic11.setName("Picture 11");
		pic11.setDescription("We are having fun :)");
		pic11.setUrl("https://europa.eu/youth/sites/default/files/article/53403505%20-%20%C2%A9%20shutterstock.com%20-%20Brocreative_0.jpg");
		Picture pic12 = new Picture();
		pic12.setId("12");
		pic12.setName("Picture 12");
		pic12.setDescription("Drinking beer with friends");
		pic12.setUrl("https://soontrending.com/wp-content/uploads/2017/12/Dollarphotoclub_81742656-300x200.jpg");
		Map<String, Picture> pictures1 = new HashMap<>();
		pictures1.put(pic11.getId(), pic11);
		pictures1.put(pic12.getId(), pic12);

		Category category1 = new Category();
		category1.setName("Vienna");
		category1.setPictures(pictures1);

		Picture pic21 = new Picture();
		pic21.setId("21");
		pic21.setName("Picture 21");
		pic21.setDescription("Drinking again...");
		pic21.setUrl("https://media-cdn.tripadvisor.com/media/photo-s/0b/96/c5/3e/1516-brewing-company.jpg");
		Map<String, Picture> pictures2 = new HashMap<>();
		pictures2.put(pic21.getId(), pic21);

		Category category2 = new Category();
		category2.setName("City");
		category2.setPictures(pictures2);

		categories.add(category1);
		categories.add(category2);
	}

	public List<Category> getCategories() {
		return categories;
	}
}
