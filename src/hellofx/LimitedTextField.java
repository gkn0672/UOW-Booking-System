package hellofx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

//Special text field for validate input

public class LimitedTextField extends TextField {
	private IntegerProperty maxLength = new SimpleIntegerProperty(this,
			"maxLength", -1);
	private StringProperty restrict = new SimpleStringProperty(this, "restrict");

	public LimitedTextField() throws Exception{
		textProperty().addListener(new ChangeListener<String>() {
			private boolean ignore;
	
			@Override
			public void changed(
					ObservableValue<? extends String> observableValue,
					String s, String s1) {
				if (ignore || s1 == null)
					return;
	
				if (maxLength.get() > -1 && s1.length() > maxLength.get()) {
					ignore = true;
				}
	
				if (restrict.get() != null && !restrict.get().equals("")
						&& !s1.matches(restrict.get() + "*")) {
					ignore = true;
				}
	
				if (ignore == true){
					setText(s);
					ignore = false;
				}
			}
		});
	}

	/**
	 * The max length property.
	 *
	 * @return The max length property.
	 */
	public IntegerProperty maxLengthProperty() {
		return maxLength;
	}

	/**
	 * Gets the max length of the text field.
	 *
	 * @return The max length.
	 */
	public int getMaxLength() {
		return maxLength.get();
	}

	/**
	 * Sets the max length of the text field.
	 *
	 * @param maxLength
	 *            The max length.
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength.set(maxLength);
	}

	/**
	 * The restrict property.
	 *
	 * @return The restrict property.
	 */
	public StringProperty restrictProperty() {
		return restrict;
	}

	/**
	 * Gets a regular expression character class which restricts the user input.
	 *
	 *
	 * @return The regular expression.
	 * @see #getRestrict()
	 */
	public String getRestrict() {
		return restrict.get();
	}

	/**
	 * Sets a regular expression character class which restricts the user input.
	 *
	 * 
	 *
	 * @param restrict
	 * 
	 *          The regular expression.
	 */

	public void setRestrict(String restrict) {
		this.restrict.set(restrict);
	}

	public void Restrictprice(){
		this.setRestrict("[0-9]?[0-9]\\.[0-9]");
		
		this.setMaxLength(11);
	}
}
