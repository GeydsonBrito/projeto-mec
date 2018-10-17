package pagamentos;

public class Discente {
	
	private String cpf;
	private String nome;
	private String programa;
	private double valorAnual; //somatorio de todos os valores recebidos pelo discente
	private boolean editado = false; //flag pra indicar que o discente já teve seu valor editado e não deve mais receber registro
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public double getValorAnual() {
		return valorAnual;
	}
	public void setValorAnual(double valorAnual) {
		this.valorAnual = valorAnual;
	}
	
	public void setEditado(boolean valorBooleano) {
		this.editado = valorBooleano;
	}
	public boolean isEditado() {
		return editado;
	}
	
	@Override
	public String toString() {
		// gerei toString só pra efetuar testes
		return nome+" - "+cpf+"  -  "+programa+"   -   "+valorAnual;
	}
	

}
