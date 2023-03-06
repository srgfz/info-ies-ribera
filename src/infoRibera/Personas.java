package infoRibera;

public abstract class Personas {
	//ATRIBUTOS
	protected int codigo;
	protected String DNI;
	protected String nombre;
	protected String apellidos;
	protected int edad;

	//CONSTRUCTORES
		//sin parametros
		public Personas() {
			super();
			this.codigo=0;
			this.DNI = "";
			this.nombre = "";
			this.apellidos = "";
			this.edad = 0;

		}
		//con parametros
		public Personas(int codigo, String dNI, String nombre, String apellidos, int edad) {
			super();
			this.codigo=codigo;
			this.DNI = dNI;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.edad = edad;

		}
	//GETTERS Y SETTRES 
		public int getCodigo() {
			return codigo;
		}
		public void setCodigo(int codigo) {
			this.codigo = codigo;
		}
		public String getDNI() {
			return DNI;
		}
		public void setDNI(String dNI) {
			DNI = dNI;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getApellidos() {
			return apellidos;
		}
		public void setApellidos(String apellidos) {
			this.apellidos = apellidos;
		}
		public int getEdad() {
			return edad;
		}
		public void setEdad(int edad) {
			this.edad = edad;
		}
		//toString
		@Override
		public String toString() {
			return "Personas [codigo=" + codigo + ", DNI=" + DNI + ", nombre=" + nombre + ", apellidos=" + apellidos
					+ ", edad=" + edad + "]";
		}
		
		//Metodo valida
		public  boolean valida(String cadena) {
			// TODO Auto-generated method stub
			boolean valido=false;
	        String[] letras = {"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"};
	        int numero = Integer.parseInt(cadena.substring(0,8));
	        String letra = letras[numero%23];
			if(cadena.matches("[0-9]{8}[A-Z]{1}") && letra.equalsIgnoreCase(String.valueOf(cadena.charAt(8)))){
				valido=true;
			}
			return valido;
		}




}
