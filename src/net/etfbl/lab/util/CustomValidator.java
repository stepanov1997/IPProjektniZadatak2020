package net.etfbl.lab.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("net.etfbl.lab.util.CustomValidator")
public class CustomValidator implements Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
		String inputText = value.toString();
		boolean containsDigit = false;

		if (inputText != null && !inputText.isEmpty()) {
			for (char c : inputText.toCharArray()) {
				if (containsDigit = Character.isDigit(c)) {
					break;
				}
			}
		}

		if (containsDigit) {	
			throw new ValidatorException(new FacesMessage("Ime sadrzi brojeve"));
		}

	}
}
