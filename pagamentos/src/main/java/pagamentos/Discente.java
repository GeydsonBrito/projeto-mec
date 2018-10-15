package pagamentos;

public class Discente {
	
	private String cpf;
	private String nome;
	private String programa;
	private double valorAnual;
	private boolean editado = false;
	
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
		// TODO Auto-generated method stub
		return nome+" - "+cpf+"  -  "+programa+"   -   "+valorAnual;
	}
	

}
