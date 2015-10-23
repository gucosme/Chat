import javax.swing.JFrame;

public class TesteServidor
{
   public static void main( String[] args )
   {
	  TelaEscolhaNick tela = new TelaEscolhaNick();
	  tela.setVisible(true);
	  tela.setAlwaysOnTop(true);
	  tela.setLocationRelativeTo(null);
	  
      Servidor application = new Servidor();
      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      application.setLocationRelativeTo(null);
      application.runServer();
   }
}