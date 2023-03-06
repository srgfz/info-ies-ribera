package infoRibera;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Profesor extends Personas{
	//Atributos
	protected String titulacion;
	protected ArrayList<Modulo> modulosImpartidos;
	
	//Constructores
	public Profesor() {
		super();
		this.titulacion="";
		this.modulosImpartidos = new ArrayList<>();
	}
	public Profesor(int codigo, String dNI, String nombre, String apellidos, int edad,String titulacion, ArrayList<Modulo> modulosImpartidos) {
		super(codigo, dNI, nombre, apellidos, edad);
		this.titulacion=titulacion;
		this.modulosImpartidos = new ArrayList<>();
	}
	//Getters y Setters
	public String getTitulacion() {
		return titulacion;
	}

	public void setTitulacion(String titulacion) {
		this.titulacion = titulacion;
	}
	
	public ArrayList<Modulo> getModulosImpartidos() {
		return modulosImpartidos;
	}

	public void setModulosImpartidos(ArrayList<Modulo> modulosImpartidos) {
		this.modulosImpartidos = modulosImpartidos;
	}
	//toString
	@Override
	public String toString() {
		return "Profesor [codigo=" + codigo + ", DNI=" + DNI + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", edad=" + edad + "]";
	}
	//Metodos
		//cargarModulosImpartidos
		public void cargaModulosImpartidos() {
			try {
				ResultSet rset=BD.getInstance().consultaBD("SELECT * FROM MODULOS WHERE COD_PROFESOR="+codigo);
		while(rset.next()){
			//Mientras haya datos
		
			//Guardo los datos en un AL
			Modulo m = new Modulo(rset.getString(1), rset.getString(2), rset.getInt(3), rset.getInt(4));
			modulosImpartidos.add(m);
		}
			rset.close();//Cerramos la instancia de miconexion

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	
}
