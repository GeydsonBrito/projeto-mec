package pagamentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	
	//adicionando o comentario no eclipse

	static ArrayList<Discente> listaDeDiscentes = new ArrayList<Discente>();
	//static ArrayList<String> naoEstaNaListaMEC = new ArrayList<String>();
	static Discente discente;
	static int somatorio = 0;

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub

		File file = new File("C:\\Users\\gleyd\\Music\\contabilidade.xlsx");

		String cpfTemporario = "";

		XSSFWorkbook book = new XSSFWorkbook(file);
		XSSFSheet sheet = book.getSheetAt(1);

		String programaAnterior = "";

		//MODIFICAR O TAMANHO DA PLANILHA DE 2017
		
		for (int i = 1; i < 27625; i++) {
			XSSFRow linha = sheet.getRow(i);
			cpfTemporario = linha.getCell(1).toString();

			// estou tratando o cpf quando ele vaio ser adicionado pela 1° vez ATENÇÂO
			// CPF aqui não está na lista
			if ((cpfEstaNaLista(cpfTemporario) == false)) {
				discente = new Discente();

				discente.setPrograma(linha.getCell(0).toString());

				if (!discente.getPrograma().equals("")) {
					programaAnterior = discente.getPrograma();
				} else {
					discente.setPrograma(programaAnterior);
				}

				discente.setCpf(linha.getCell(1).toString());
				discente.setNome(linha.getCell(2).toString());
				discente.setValorAnual(Double.parseDouble(linha.getCell(3).toString()));

				listaDeDiscentes.add(discente);
			} else {
				double valorAtual = discente.getValorAnual();
				double soma = Double.parseDouble(linha.getCell(3).toString());
				discente.setValorAnual(valorAtual + soma);
			}

			// System.out.println(discente.toString());
		}

		// gerarPlanilha(listaDeDiscentes);
		alterarValorDiscentesPNAES(listaDeDiscentes);
		System.out.println(somatorio);

	}

	public static boolean cpfEstaNaLista(String cpfTemporario) {
		for (Discente d : listaDeDiscentes) {
			if (d.getCpf().equals(cpfTemporario)) {
				discente = d;
				return true;
			}
		}
		return false;
	}

	public static void gerarPlanilha(ArrayList<Discente> lista) throws IOException {

		FileOutputStream outFile = new FileOutputStream(new File("C:\\Users\\gleyd\\Desktop\\resultado.xlsx"));
		
		@SuppressWarnings("resource")
		XSSFWorkbook w = new XSSFWorkbook();
		XSSFSheet s = w.createSheet();
		int linha = 0;
		for (Discente d : lista) {
			XSSFRow row = s.createRow(linha);
			XSSFCell nome = row.createCell(0);
			nome.setCellValue(d.getNome());

			XSSFCell cpf = row.createCell(1);
			cpf.setCellValue(d.getCpf());

			XSSFCell programa = row.createCell(2);
			programa.setCellValue(d.getPrograma());

			XSSFCell valor = row.createCell(3);
			valor.setCellValue(d.getValorAnual());

			linha++;
		}

		w.write(outFile);
		outFile.close();
		System.out.println("Arquivo Excel editado com sucesso!");
	}

	public static void alterarValorDiscentesPNAES(ArrayList<Discente> discentes)
			throws InvalidFormatException, IOException {
		String cpfPNAES = "";
		// começa na linha 0 até 3416
		File file = new File("C:\\Users\\gleyd\\Music\\consulta.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook workbookMEC = new XSSFWorkbook(file);
		XSSFSheet aba = workbookMEC.getSheetAt(0);

		for (int i = 0; i < 3416; i++) {
			XSSFRow linha = aba.getRow(i);
			cpfPNAES = linha.getCell(2).toString(); // pega o cpf da planilha do MEC
			int posicao = cpfEstaNaListaCONTABILIDADE(cpfPNAES.toString()); // consulta se esse cpf esta na planilha do
			// contabilidade

			if (posicao > 1) {
				double valorParaSubstituir = discentes.get(posicao).getValorAnual(); // pega o valor que esta na

				if (discentes.get(posicao).isEditado() == false) {
					XSSFCell celula = linha.createCell(20);
					celula.setCellValue(valorParaSubstituir);
					discentes.get(posicao).setEditado(true);
					somatorio++;
				}
			} /*
				 * else { discentes.get(posicao).setEditado(true); }
				 */

		}

		FileOutputStream outFile = new FileOutputStream(new File("C:\\Users\\gleyd\\Desktop\\Resultado_PNAES.xlsx"));
		workbookMEC.write(outFile);
		outFile.close();
		System.out.println("Arquivo PNAES editado com sucesso!");
	}

	public static int cpfEstaNaListaCONTABILIDADE(String cpfPNAES) {
		for (Discente d : listaDeDiscentes) {
			if (d.getCpf().equals(cpfPNAES)) {
				return listaDeDiscentes.indexOf(d);

			}
		}
		return -1;
	}

}
