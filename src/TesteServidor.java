import javax.swing.JFrame;

public class TesteServidor
{
   public static void main( String[] args )
   {
      Servidor application = new Servidor();
      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      application.runServer();
   }
}