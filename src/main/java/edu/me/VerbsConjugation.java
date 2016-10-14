package edu.me;

public class VerbsConjugation {
	private String infinitiv;
	private PersonForms presente;
	private PersonForms preterito;
	private PersonForms futuro;
	private PersonForms condicional;
	private PersonForms imperfecto;

	public String getInfinitiv() {
		return infinitiv;
	}

	public VerbsConjugation setInfinitiv(String infinitiv) {
		this.infinitiv = infinitiv;
		return this;
	}

	public PersonForms getPresente() {
		return presente;
	}

	public VerbsConjugation setPresente(PersonForms presente) {
		this.presente = presente;
		return this;
	}

	public PersonForms getPreterito() {
		return preterito;
	}

	public VerbsConjugation setPreterito(PersonForms preterito) {
		this.preterito = preterito;
		return this;
	}

	public PersonForms getFuturo() {
		return futuro;
	}

	public VerbsConjugation setFuturo(PersonForms futuro) {
		this.futuro = futuro;
		return this;
	}

	public PersonForms getCondicional() {
		return condicional;
	}

	public VerbsConjugation setCondicional(PersonForms condicional) {
		this.condicional = condicional;
		return this;
	}

	public PersonForms getImperfecto() {
		return imperfecto;
	}

	public VerbsConjugation setImperfecto(PersonForms imperfecto) {
		this.imperfecto = imperfecto;
		return this;
	}
}
