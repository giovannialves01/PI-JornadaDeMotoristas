package model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.JOptionPane;

@Entity
@Table(name="funcionarios")
public class Funcionario {
	

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String cpf;
	private String nome;
	private String senha;
	private String cargo;
	private int fk_filiais_id;
	
public Funcionario(String cpf, String nome, String senha, String cargo, String k_filiais_id) {
		this.cpf = cpf;
		this.nome = nome;
		this.senha = senha;
		this.cargo = cargo;
		this.fk_filiais_id = fk_filiais_id;
		
		this.cadastrarMotorista();
	}

//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	private Integer id;
	
	public JornadaTrabalho cadastrarJornadaTrabalho(String funcionario, int carga_horaria, String turno, boolean seg, boolean ter, boolean qua, boolean qui, boolean sex, boolean sab, boolean dom) {
		
		EntityManager con = new ConnectionFactory().getConnection();
		JornadaTrabalho jornada = new JornadaTrabalho();
		
		jornada.setFk_funcionarios_cpf(funcionario);
		jornada.setCarga_horaria(carga_horaria);
		jornada.setTurno(turno);
		jornada.setSeg(seg);
		jornada.setTer(ter);
		jornada.setQua(qua);
		jornada.setQui(qui);
		jornada.setSex(sex);
		jornada.setSab(sab);
		jornada.setDom(dom);
		
		try {
			//Mandando os dados do trabalho do motorista para a tabela de jornadas de trabalho
			con.getTransaction().begin();
			con.persist(jornada);
			con.getTransaction().commit();
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Jornada de trabalho. Tente novamente.\nErro: "+ e, "Erro", JOptionPane.ERROR_MESSAGE);
			con.getTransaction().rollback();
		}
		finally {
			con.close();
		}
		
		return jornada;
		
	}
	
	public void cadastrarMotorista() {

		EntityManager con = new ConnectionFactory().getConnection();
		
		
		try {
			
			//Mandando os dados do motorista para a tabela de motoristas
			con.getTransaction().begin();
			con.persist(this);
			con.getTransaction().commit();
			
			if (this.cargo == "motorista") {
				cadastrarJornadaTrabalho(this.cpf, 8, "manh�", true, true, true, true, true, false, false);
			}
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Funcion�rio. Tente novamente.\nErro: "+ e, "Erro", JOptionPane.ERROR_MESSAGE);
			con.getTransaction().rollback();
		}
		finally {
			con.close();
		}
		
	}
	
	public Filial cadastrarFilial(String nome, String cidade, String uf) {
		EntityManager con = new ConnectionFactory().getConnection();
		Filial filial = new Filial();
		
		filial.setNome(nome);
		filial.setCidade(cidade);
		filial.setEstado(uf);	
		
		try {
			con.getTransaction().begin();
			con.persist(filial);
			con.getTransaction().commit();
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Erro: "+ e, "Erro", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			con.close();
		}
		
		return filial;
		
	}
	
	public void alterarDadosFuncionario(String novoNome, String cpfFuncionario, String novaSenha, String novoCargo, int novaFilial) {
		//lembrando que esse m�todo t� bem simples ainda. n�o tem verifica��o de nada
		EntityManager con = new ConnectionFactory().getConnection();
		
		this.nome = novoNome;
		this.cpf = cpfFuncionario;
		this.senha = novaSenha;
		this.cargo = novoCargo;
		this.fk_filiais_id = novaFilial;
		
		try {
			con.getTransaction().begin();
			con.merge(this);
			con.getTransaction().commit();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro: "+ e, "Erro", JOptionPane.ERROR_MESSAGE);
			con.getTransaction().rollback();
		}
		finally {
			con.close();
		}
		
	}
	
}