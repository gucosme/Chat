import javax.swing.JFrame;

public class TesteCliente 
{
   public static void main( String[] args )
   {
      Cliente application;

      if ( args.length == 0 )
         application = new Cliente( "127.0.0.1" );
      else
         application = new Cliente( args[ 0 ] );

      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      application.runClient();
   }
}
