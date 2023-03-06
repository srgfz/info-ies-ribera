package infoRibera;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


public class BD {
	//Atributos:
		private static BD miInstancia=null;
		private static boolean permitirInstancianueva;
		private String cadenaConexion;
		private String usuario;
		private String password;
		private Connection conn;
		private Statement stmt;
	//Con el booleano permitirInstancianueva controlar que solo exista una
		BD() throws Exception{
			if(!permitirInstancianueva)
				throw new Exception("Ya hay una instancia greadea; usa getInstance()");
		}
	//La primera y unica vez que se crea
	//Fijate que se inicializa a nulo al declarar la variable de la clase
		public static BD getInstance() {
			if(miInstancia==null) {//Si es la primera vez que creo la instancia
				permitirInstancianueva=true;
				try {
					miInstancia=new BD();//Anadimos el try catch por la excepcion que lanza el BD()
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//Creo la instancia
				permitirInstancianueva=false;//Despues de crear lo vuelvo a poner a false
			}
			
			return miInstancia;
		}
	//Devuelve el conjunto de tuplas de la consulta
		public ResultSet consultaBD(String consulta) throws SQLException {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn=DriverManager.getConnection(cadenaConexion, usuario, password);
			stmt=conn.createStatement();
			ResultSet rset=stmt.executeQuery(consulta);
			
			return rset;
		 }
	//Cerrar consulta
		public void cerrarConsulta() throws SQLException {
			stmt.close();
		 }
		//Getters y Setters
		public String getCadenaConexion() {
			return cadenaConexion;
		}
		public void setCadenaConexion(String cadenaConexion) {
			this.cadenaConexion = cadenaConexion;
		}
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Connection getConn() {
			return conn;
		}
		public void setConn(Connection conn) {
			this.conn = conn;
		}
		public Statement getStmt() {
			return stmt;
		}
		public void setStmt(Statement stmt) {
			this.stmt = stmt;
		}

	//Otros metodos para
		//Cargar Parametros de conexion
			public boolean cargarParametrosConexion() {
				File fichero = new File("config.txt");
				String cadena = ""; //Donde guardare los datos que vaya a leer 
				Scanner entrada = null;
				try {
					entrada = new Scanner(fichero);
					String[] linea;
					while (entrada.hasNext()) { //Lee si quedan datos en el fichero
						cadena = entrada.nextLine();
						linea = cadena.split("=");
						if(linea[0].equals("cadena")) {
							setCadenaConexion(linea[1]);
						}
						if(linea[0].equals("usuario")) {
							setUsuario(linea[1]);
						}
						if(linea[0].equals("password")) {
							setPassword(linea[1]);
						}
					}
					entrada.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("No existe el fichero");
					e.printStackTrace();
				}
				//Hago una consulta de prueba para ver si se ha conectado correctamente a la BD
				BD miconexion = BD.getInstance();
				try {
					ResultSet rset=miconexion.consultaBD("SELECT * FROM DUAL");
					miconexion.cerrarConsulta();
				return true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
			}
		//cargaNotas
			public void cargaNotas(String fichero) {
				File ficherocsv = new File(fichero);
				try {
					Scanner entrada = new Scanner(ficherocsv);
					String cadena = "", codAlumno = "", codAlumnoAnterior="", codModulo="";
					double sumaNotas=0.0;
					int contNotas=0, contAlumnos=1;
					String[] linea;
					entrada.nextLine();
					while (entrada.hasNext()) { //Lee si quedan datos en el fichero
						cadena = entrada.nextLine();
						linea = cadena.split(";"); //Aqui se guardaran los datos
						
							codAlumno=linea[1];
							codModulo=linea[0];//El modulo es el mismo siempre en todo el archivo csv
						
						if(codAlumno.equals(codAlumnoAnterior) || codAlumnoAnterior.equals("")) {
							sumaNotas+=Double.parseDouble(linea[3]);
							contNotas++;
						} else {//Aqui es la linea donde cambia el alumno
							//consulta para meter datos a la BD:
							try {
								ResultSet rset=getInstance().consultaBD("INSERT INTO NOTAS VALUES('"+codModulo+"',"+codAlumnoAnterior+","+(sumaNotas/contNotas)+")");
								rset = getInstance().consultaBD("COMMIT");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								BD.getInstance().cerrarConsulta();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							sumaNotas=Double.parseDouble(linea[3]);
							contNotas=1;
							contAlumnos++;
						}
						codAlumnoAnterior=codAlumno;
					}
					entrada.close();
					//consulta para meter datos a la BD del ultimo alumno que aparezca:
					try {
						ResultSet rset=getInstance().consultaBD("INSERT INTO NOTAS VALUES('"+codModulo+"',"+codAlumnoAnterior+","+(sumaNotas/contNotas)+")");
						rset = getInstance().consultaBD("COMMIT");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						BD.getInstance().cerrarConsulta();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Cargada la nota final de "+contAlumnos+" alumnos para el módulo "+codModulo);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("El fichero de notas no existe");
					e.printStackTrace();
				}
			}
		//cargaProfesores
			public ArrayList<Profesor> cargaProfesores() {

				ArrayList<Profesor> profesores = new ArrayList<>();
				try {
					ResultSet rset=getInstance().consultaBD("SELECT * FROM PERSONAS WHERE TITULACION IS NOT NULL");
			while(rset.next()){//Mientras haya datos
				//Guardo los datos en un AL
				Profesor p = new Profesor(rset.getInt(1), rset.getString(2), rset.getString(3),  rset.getString(4), rset.getInt(5),  rset.getString(6), new ArrayList<>());
				p.cargaModulosImpartidos();
				profesores.add(p);
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				try {
					BD.getInstance().cerrarConsulta();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return profesores;
			}
		//cargaAlumnos
			public ArrayList<Alumno> cargaAlumnos() {
				ArrayList<Alumno> alumnos = new ArrayList<>();
				try {
					ResultSet rset=getInstance().consultaBD("SELECT * FROM PERSONAS WHERE TITULACION IS NULL");
			while(rset.next()){//Mientras haya datos
					Alumno a = new Alumno(rset.getInt(1), rset.getString(2), rset.getString(3),  rset.getString(4), rset.getInt(5),0.0); 
					a.cargaMediaExpediente();
					alumnos.add(a);	
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				try {
					BD.getInstance().cerrarConsulta();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return alumnos;
			}
		//cargaAlumnoParametro
			public ArrayList<Alumno> cargaAlumnoParametro(int codAlumno) {
				ArrayList<Alumno> alumnos = new ArrayList<>();
				try {
					ResultSet rset=getInstance().consultaBD("SELECT * FROM PERSONAS WHERE TITULACION IS NULL");
			while(rset.next()){//Mientras haya datos
				if(codAlumno==rset.getInt(1)) {
					Alumno a = new Alumno(rset.getInt(1), rset.getString(2), rset.getString(3),  rset.getString(4), rset.getInt(5),0.0); 
					a.cargaMediaExpediente();
					alumnos.add(a);	
				}
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				try {
					BD.getInstance().cerrarConsulta();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return alumnos;
			}
		//cargaProfesorParametro
			public ArrayList<Profesor> cargaProfesorParametro(int codProfesor) {
				ArrayList<Profesor> profesores = new ArrayList<>();
				try {
					ResultSet rset=getInstance().consultaBD("SELECT * FROM PERSONAS WHERE TITULACION IS NOT NULL");
			while(rset.next()){//Mientras haya datos
				if(codProfesor==rset.getInt(1)) {
					//Guardo los datos en un AL
					Profesor p = new Profesor(rset.getInt(1), rset.getString(2), rset.getString(3),  rset.getString(4), rset.getInt(5),  rset.getString(6), new ArrayList<>());
					p.cargaModulosImpartidos();
					profesores.add(p);
				}
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				try {
					BD.getInstance().cerrarConsulta();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return profesores;
			}
}