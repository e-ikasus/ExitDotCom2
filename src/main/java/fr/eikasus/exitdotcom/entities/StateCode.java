package fr.eikasus.exitdotcom.entities;

public enum StateCode
{
	OP("OPENED"),
	CL("CLOSED"),
	CU("CURRENT"),
	EN("ENDED"),
	CA("CANCELED"),
	AR("ARCHIVED"),
	CR("CREATING");

	private final String value;

	StateCode(String value) {this.value = value;}

	public String getValue() {return value;}
}
