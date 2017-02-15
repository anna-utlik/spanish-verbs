package edu.me;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class Main {

	public static void main(String[] args) {

		Set<String> inputVerbs = new HashSet<>();
		inputVerbs.addAll(Arrays.asList("ser"
				, "ir"
				, "ver"
				, "dar"
				, "saber"
				, ""
				, "parecer"
				, "conocer"
				, "aparecer"
				, "producir"
				, "reconocer"
				, "nacer"
				, "ofrecer"
				, "crecer"
				, "establecer"
				, "desaparecer"
				, ""
				, "poder"
				, "encontrar"
				, "volver"
				, "recordar"
				, "morir"
				, "mover"
				, "contar"
				, "costar"
				, "soler"
				, "mostrar"
				, "resolver"
				, "demostrar"
				, "acordar"
				, ""
				, "decir"
				, "tener"
				, "poner"
				, "seguir"
				, "venir"
				, "salir"
				, "mantener"
				, "caer"
				, "traer"
				, "suponer"
				, "oír"
				, "valer"
				, "imponer"
				, "proponer"
				, "obtener"
				, "detener"
				, ""
				, "sentir"
				, "querer"
				, "pensar"
				, "empezar"
				, "perder"
				, "entender"
				, "convertir"
				, "referir"
				, "cerrar"
				, "preferir"
				, "comenzar"
				, "elegir"
				, "negar"
				, "adquirir"
				, ""
				, "servir"
				, "pedir"
				, "repetir"
				, ""
				, "jugar"
				, ""
				, "dirigir"
				, "elegir"
				, "recoger"
				, "exigir"
				, "surgir"
				, ""
				, "llegar"
				, "creer"
				, "buscar"
				, "escribir"
				, "conseguir"
				, "sacar"
				, "leer"
				, "abrir"
				, "realizar"
				, "explicar"
				, "tocar"
				, "alcanzar"
				, "utilizar"
				, "pagar"
				, "descubrir"
				, "continuar"
				, "acercar"
				, "dedicar"
				, "indicar"
				, "significar"
				, "reunir"
				, "construir"
				, "andar"
				, "entregar"
				, "colocar"
				, "actuar"
				, "romper"
				, "lanzar"
				, "avanzar"
				, "obligar"
				, "aplicar"));

		ExecutorService executorService = Executors.newFixedThreadPool(1);
		executorService.submit(() -> {
					Main main = new Main();
					try {
						main.writeVerbs();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Hello)");
				}
		);
	}

	private Map<String, VerbsConjugation> searchForVerbs(Collection<String> inputVerbs) throws IOException {
		Map<String, VerbsConjugation> verbs = new HashMap<>();

		FileInputStream fileInputStream = new FileInputStream("src/main/resources/verbs.xls");
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(fileInputStream);
		HSSFSheet sheet = xssfWorkbook.getSheet("conjug");
		Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String verbInf = row.getCell(1).getStringCellValue();
			if (!inputVerbs.contains(verbInf)) {
				continue;
			}
			VerbsConjugation conjugation = verbs.get(verbInf);
			if (conjugation == null) {
				conjugation = new VerbsConjugation();
				conjugation.setInfinitiv(verbInf);
				verbs.put(verbInf, conjugation);
			}
			String t = row.getCell(3).getStringCellValue();
			Tiempo tiempo;
			try {
				tiempo = Tiempo.valueOf(t);
			} catch (IllegalArgumentException e) {
				continue;
			}
			switch (tiempo) {
			case Presente:
				conjugation.setPresente(createPersonForm(row));
				break;
			case Pretérito:
				conjugation.setPreterito(createPersonForm(row));
				break;
			case Futuro:
				conjugation.setFuturo(createPersonForm(row));
				break;
			case Condicional:
				conjugation.setCondicional(createPersonForm(row));
				break;
			case Imperfecto:
				conjugation.setImperfecto(createPersonForm(row));
				break;
			default:
				break;
			}
		}
		return verbs;
	}

	private void writeVerbs() throws IOException {
		FileInputStream fileInputStream = new FileInputStream("src/main/resources/template.xls");
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(fileInputStream);
		Tiempo[] tiempos = Tiempo.values();

		for (Tiempo tiempo : tiempos) {
			HSSFSheet sheet = xssfWorkbook.getSheet(tiempo.name());
			List<String> verbs = getVerbs(sheet.rowIterator());
			Map<String, VerbsConjugation> conjugationMap = searchForVerbs(verbs);
			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String verbInf = row.getCell(1).getStringCellValue();

				VerbsConjugation verbsConjugation = conjugationMap.get(verbInf);
				if (verbsConjugation == null) {
					continue;
				}
				PersonForms forms;
				switch (tiempo) {
				case Presente:
					forms = verbsConjugation.getPresente();
					break;
				case Pretérito:
					forms = verbsConjugation.getPreterito();
					break;
				case Imperfecto:
					forms = verbsConjugation.getImperfecto();
					break;
				case Condicional:
					forms = verbsConjugation.getCondicional();
					break;
				case Futuro:
					forms = verbsConjugation.getFuturo();
					break;
				default:
					forms = new PersonForms();
					break;
				}
				writePersonForm(forms, row);
			}
		}
		try (FileOutputStream outputStream = new FileOutputStream("target/myVerbs.xls")) {
			xssfWorkbook.write(outputStream);
		}
	}

	private List<String> getVerbs(Iterator<Row> rowIterator) {
		List<String> verbs = new ArrayList<>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row next = rowIterator.next();
			verbs.add(next.getCell(1).getStringCellValue());
		}
		return verbs;
	}

	private PersonForms createPersonForm(Row row) {
		PersonForms personForms = new PersonForms();
		personForms.setYo(row.getCell(4).getStringCellValue());
		personForms.setTu(row.getCell(5).getStringCellValue());
		personForms.setEl(row.getCell(6).getStringCellValue());
		personForms.setNosotros(row.getCell(7).getStringCellValue());
		personForms.setVosotros(row.getCell(8).getStringCellValue());
		personForms.setEllos(row.getCell(9).getStringCellValue());
		return personForms;
	}

	private void writePersonForm(PersonForms forms, Row row) {
		((HSSFRow) row).createCell(2).setCellValue(new HSSFRichTextString(forms.getYo()));
		((HSSFRow) row).createCell(3).setCellValue(new HSSFRichTextString(forms.getTu()));
		((HSSFRow) row).createCell(4).setCellValue(new HSSFRichTextString(forms.getEl()));
		((HSSFRow) row).createCell(5).setCellValue(new HSSFRichTextString(forms.getNosotros()));
		((HSSFRow) row).createCell(6).setCellValue(new HSSFRichTextString(forms.getVosotros()));
		((HSSFRow) row).createCell(7).setCellValue(new HSSFRichTextString(forms.getEllos()));
	}

}
