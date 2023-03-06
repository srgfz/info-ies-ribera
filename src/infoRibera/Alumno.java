package infoRibera;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Alumno extends Personas{
	//Atributos
	private double mediaExpediente=0.0;
	//Constructores
	public Alumno() {
		super();
		mediaExpediente = 0.0;	
	}
	public Alumno(int codigo, String dNI, String nombre, String apellidos, int edad, double mediaExpediente) {
		super(codigo, dNI, nombre, apellidos, edad);
		mediaExpediente = 0.00;	
	}
	//Getters y Setters
	public double MediaExpediente() {
		return mediaExpediente;
	}
	public void setMediaExpediente(double mediaExpediente) {
		this.mediaExpediente = mediaExpediente;
	}
	//toString
	@Override
	public String toString() {
		return "Alumno [codigo=" + codigo + ", DNI=" + DNI + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", edad=" + edad + "]";
	}
	//Metodos
		//cargaMediaExpediente
		public void cargaMediaExpediente() {
			double mediaExpediente=0.0;
			int contNotas=0;
			try {
				ResultSet rset=BD.getInstance().consultaBD("SELECT * FROM NOTAS WHERE COD_ALUMNO="+codigo);
			while(rset.next()){
				//Mientras haya datos
				if(!Double.isNaN(rset.getDouble(3))) {
					mediaExpediente+=rset.getDouble(3);
					contNotas++;
					}
			}
				rset.close();//Cerramos la instancia de miconexion
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Hacemos el setter solo en los alumnos con datos en la BD
			if(!Double.isNaN(mediaExpediente/contNotas)) {
				setMediaExpediente(mediaExpediente/contNotas);
			}
		}
}
