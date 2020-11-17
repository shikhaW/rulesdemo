package com.nse.excelupload.model;

import java.io.Serializable;

public class Cell implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

  	private String cellName = null;
  	
  	private boolean isValid=true;
  	
  	private String validationMsg;

	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	private Object value;

	// Default constructor - needed to keep this a Javabean
	public Cell() {

	}

	public Cell(String cellName, Object value) {
		super();
		this.cellName = cellName;
		this.value = value;
	}

	public String getCellName() {
		return cellName;
	}

	public Object getValue() {
		return value;
	}
	
	/**
	 * Convenience method
	 * @return
	 */
	public void setCellName(String cellName) {

		String oldValue = this.cellName;
		this.cellName = cellName;
	
	}


	public void setValue(Object value) {
		Object oldValue = this.value;
		this.value = value;
	}

	@Override
	public String toString() {
		return "cellName:" + cellName + " value:" + value +" isValid:" + isValid + " ValidationMsg" + validationMsg;
	} 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cellName == null) ? 0 : cellName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (cellName == null) {
			if (other.cellName != null)
				return false;
		} else if (!cellName.equals(other.cellName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}






}
