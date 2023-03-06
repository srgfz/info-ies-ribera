package infoRibera;

public class Modulo {
	//Atributos
	protected String codigo;
	protected String nombre;
	protected int horas;
	protected int curso;
	//Constructores
	public Modulo() {
		super();
		this.codigo = "";
		this.nombre = "";
		this.horas = 0;
		this.curso = 0;
	}
	public Modulo(String codigo, String nombre, int horas, int curso) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.horas = horas;
		this.curso = curso;
	}
	//Getters y Setters
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getHoras() {
		return horas;
	}
	public void setHoras(int horas) {
		this.horas = horas;
	}
	public int getCurso() {
		return curso;
	}
	public void setCurso(int curso) {
		this.curso = curso;
	}
	//toString
	@Override
	public String toString() {
		return "Módulo [codigo=" + codigo + ", nombre=" + nombre + ", horas=" + horas + ", curso=" + curso + "]";
	}
	

}
