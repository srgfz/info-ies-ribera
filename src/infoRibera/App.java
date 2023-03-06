package infoRibera;

import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
	public static void main(String[] args){
		Boolean existe=false;
		if(args.length>0) {//Hay parametros
			String[] parametro=args[0].split("=");
			if(args[0].contains("--profesor")) {//1. El programa recibe el parametro --profesor
				paramProfesor(existe, parametro);
			}else if(args[0].contains("--fichero-notas")) {//2. El programa recibe el parametro --fichero-notas				
				paramFicheroNotas(parametro);
			}else if(args[0].contains("--alumno")) {//3. El programa recibe el parametro --alumno
				paramAlumno(existe, parametro);			
			}else if(args[0].contains("--informe-notas")) {//4. El programa recibe el parametro --informe-notas
				paramNotasMedias();
		}
	} else {// 5. El programa no recibe ningun parametro
		paramNone();
	}
}

//METODOS
	//Parametro: codigo Profesor
	public static void paramProfesor(Boolean existe, String[] parametro) {
		BD miconexion = BD.getInstance();
		miconexion.cargarParametrosConexion();
		int codProfesor = Integer.parseInt(parametro[1]);
		int horasDocencia=0;
		for (Profesor x : miconexion.cargaProfesorParametro(codProfesor)) {
			if(x.getCodigo()==codProfesor) {
				existe=true;
				System.out.println("Módulos que imparte el profesor "+x.getNombre()+" "+x.getApellidos());
				for (Modulo y : x.modulosImpartidos) {
					System.out.println(y.getCodigo()+" - "+y.getNombre());
					horasDocencia+=y.getHoras();
				}
				System.out.println("Horas de docencia: "+ horasDocencia);
			}
		}
		try {
			miconexion.cerrarConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!existe) {
			System.out.println("Profesor inexistente");
		}
	}
	//Parametro: fichero notas
	public static void paramFicheroNotas(String[] parametro) {
		BD miconexion = BD.getInstance();
		miconexion.cargarParametrosConexion();
		String ficheroNotas = parametro[1];
		miconexion.cargaNotas(ficheroNotas);
		try {
			miconexion.cerrarConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Parametro: codigo Alumno
	public static void paramAlumno(Boolean existe, String[] parametro) {
		BD miconexion = BD.getInstance();
		miconexion.cargarParametrosConexion();
		int codAlumno = Integer.parseInt(parametro[1]);
		//Filtrar haciendo una consulta a la base de datos con una consulta con el codAlumno del parametro:
		//Hacer otro metodo al que le paso el codAlumno
		for (Alumno x : miconexion.cargaAlumnoParametro(codAlumno)) {
			if(x.getCodigo()==codAlumno) {
				existe=true;
				System.out.println("");
				System.out.println("Notas del alumno "+x.getNombre()+" "+x.getApellidos());
				//Hago la consulta a la BD para las notas del alumno con el codigo correspondiente:
				try {
					ResultSet rset2=miconexion.consultaBD("SELECT NOTA_FINAL, NOMBRE FROM NOTAS INNER JOIN MODULOS ON NOTAS.COD_MODULO=MODULOS.CODIGO WHERE COD_ALUMNO="+codAlumno+" ORDER BY MODULOS.NOMBRE DESC");
						
					while(rset2.next()){//Mientras haya datos
							System.out.println(String.format("%05.2f",rset2.getDouble(1))+" - "+rset2.getString(2));
						}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				System.out.println("Nota media "+String.format("%05.2f",x.MediaExpediente())+"");
			}
		}
		try {
			miconexion.cerrarConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!existe) {
			System.out.println("Alumno inexistente");
		}
	}
	//Parametro: notas medias
	public static void paramNotasMedias() {
		BD miconexion = BD.getInstance();
		miconexion.cargarParametrosConexion();
		System.out.println("Notas medias");
				//Hago la consulta a la BD para las notas del alumno:
				try {
					ResultSet rset=miconexion.consultaBD("SELECT SUM(NOTA_FINAL)/COUNT(COD_ALUMNO), NOMBRE, APELLIDOS FROM NOTAS INNER JOIN PERSONAS ON notas.cod_alumno=personas.codigo GROUP BY COD_ALUMNO, NOMBRE, APELLIDOS");
					ResultSet rset2=miconexion.consultaBD("SELECT SUM(NOTA_FINAL)/COUNT(COD_ALUMNO) FROM NOTAS");
						while(rset.next()){//Mientras haya datos
							System.out.println(String.format("%05.2f",rset.getDouble(1))+" - "+rset.getString(2)+" "+rset.getString(3));
						}
						while(rset2.next()){//Mientras haya datos
							System.out.println(String.format("Nota media de todos los alumnos "+"%05.2f",rset2.getDouble(1)));
						}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				try {
					miconexion.cerrarConsulta();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	//Ningun parametro
	public static void paramNone() {
		BD miconexion = BD.getInstance();
		miconexion.cargarParametrosConexion();
		for (Profesor x : miconexion.cargaProfesores()) {
			System.out.println(x);
			for (Modulo y : x.getModulosImpartidos()) {
				System.out.println("\t"+y);
			}
		}
		System.out.println();
		for (Alumno x : miconexion.cargaAlumnos()) {

			System.out.println(x);
			System.out.println("\tMedia de su expediente "+String.format("%05.2f",x.MediaExpediente()));
		}
		try {
			miconexion.cerrarConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

