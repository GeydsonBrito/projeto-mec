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

	static ArrayList<Discente> listaDeDiscentes = new ArrayList<Discente>();
	static Discente discente;
	static int somatorio = 0;

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InvalidFormatException, IOException {

		File file = new File("C:\\Users\\gleyd\\Music\\2017_Contabilidade.xlsx");

		String cpfTemporario = "";

		XSSFWorkbook book = new XSSFWorkbook(file);
		int abaEmQueEstaoOsDados = 0;
		XSSFSheet sheet = book.getSheetAt(abaEmQueEstaoOsDados);

		String programaAnterior = "";
		int linhaInicial = 1;
		int linhaFinal = 29259;

		for (int i = linhaInicial; i < linhaFinal; i++) {
			XSSFRow linha = sheet.getRow(i);
			cpfTemporario = linha.getCell(1).toString();

			// aqui estou tratando o cpf quando ele vai ser adicionado pela 1° vez ATENÇÂO
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

		}

		// @ métodod gera a planilha a partir da planilha do contabilidade com o
		// somatório dos valores de cada aluno
		// gerarPlanilha(listaDeDiscentes);

		// @ método para gerar a planilha do MEC alterada pelos valores obtidos pela
		// planilha do contabilidade
		alterarValorDiscentesPNAES(listaDeDiscentes);

		// System.out.println(somatorio);

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

		// onde será salvo o arquivo que extrai as informações da planilha da
		// contabilidade e gera o novo arquivo .xlsx
		FileOutputStream outFile = new FileOutputStream(new File("C:\\Users\\gleyd\\Desktop\\resultado2017.xlsx"));

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

		// Define o caminhio para a planilha do MEC
		String caminhoArquivo = "C:\\Users\\gleyd\\Desktop\\AtendimentosPNAES2017_UFRPE.xlsx";

		File file = new File(caminhoArquivo);

		@SuppressWarnings("resource")
		XSSFWorkbook workbookMEC = new XSSFWorkbook(file);
		
		int abaQueEstaoOsDados = 0;
		XSSFSheet aba = workbookMEC.getSheetAt(abaQueEstaoOsDados);

		int linhaInicial = 16;
		int linhaFinal = 3410;

		for (int i = linhaInicial; i < linhaFinal; i++) {
			XSSFRow linha = aba.getRow(i);

			cpfPNAES = linha.getCell(2).toString(); // pega o cpf da planilha do MEC

			int posicao = cpfEstaNaListaCONTABILIDADE(cpfPNAES.toString()); // consulta se esse cpf esta na planilha da
																			// contabilidade eretorna sua posição

			if (posicao > 1) {
				double valorParaSubstituir = discentes.get(posicao).getValorAnual(); // pega o valor que esta na

				if (discentes.get(posicao).isEditado() == false) {
					XSSFCell celula = linha.createCell(20);
					celula.setCellValue(valorParaSubstituir);

					XSSFCell celulaPrograma = linha.getCell(21);
					if (celulaPrograma == null) {
						XSSFCell celulaP = linha.createCell(21);
						celulaP.setCellValue(discentes.get(posicao).getPrograma());
					}
					discentes.get(posicao).setEditado(true);// essa flag serve para evitar duplicidade de registrio
					somatorio++;
				} else {
					XSSFCell celulaP = linha.createCell(21);
					celulaP.setCellValue(discentes.get(posicao).getPrograma());

				}
			}

		}

		// Define o nome e local de salvamento do arquivo que foi editado: a planilha do
		// MEC com os valores carregados pela planilha da contabilidade
		String caminhoArquivoRetorno = "C:\\Users\\gleyd\\Desktop\\Resultado_PNAES2017.xlsx";

		FileOutputStream outFile = new FileOutputStream(new File(caminhoArquivoRetorno));
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
