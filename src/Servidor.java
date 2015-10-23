import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Servidor extends JFrame 
{
   
   private static final long serialVersionUID = 1L;
   private JTextField enterField;
   private JTextArea displayArea;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private ServerSocket server;
   private Socket connection;
   private int counter = 1; 
   private TelaEscolhaNick telaServidor = new TelaEscolhaNick();

   public Servidor()
   {
	  super("Chat Servidor");	  
	  
	  telaServidor.setVisible(true);
	  telaServidor.setAlwaysOnTop(true);
	  telaServidor.setLocationRelativeTo(null);
	  
	  enterField = new JTextField();
      enterField.setEditable( false );
      enterField.addActionListener(
         new ActionListener() 
         {
            public void actionPerformed( ActionEvent event )
            {
               sendData( event.getActionCommand() );
               enterField.setText( "" );
               enterField.setBackground(Color.cyan);
            }
         }
      );

      add( enterField, BorderLayout.NORTH );

      displayArea = new JTextArea();
      add( new JScrollPane( displayArea ), BorderLayout.CENTER );

      setSize( 361, 424 );
      setVisible( true );
   }

   public void runServer()
   {
      try
      {
         server = new ServerSocket( 12345, 100 );

         while ( true ) 
         {
            try 
            {
               waitForConnection();
               getStreams();
               processConnection();
            }
            catch ( EOFException eofException ) 
            {
               displayMessage( "\nConexão com Servidor terminada" );
            }
            finally 
            {
               closeConnection();
               ++counter;
            }
         }
      }
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      }
   }

   private void waitForConnection() throws IOException
   {
      displayMessage( "Aguardando por conexão\n" );
      connection = server.accept();           
      displayMessage( "Conexão " + counter + " recebida de: " +
         connection.getInetAddress().getHostName() );
   }

   private void getStreams() throws IOException
   {
      output = new ObjectOutputStream( connection.getOutputStream() );
      output.flush();

      input = new ObjectInputStream( connection.getInputStream() );

   }

   private void processConnection() throws IOException
   {
      String message = "Sucesso na conexão";
      sendData( message );

      setTextFieldEditable( true );

      do
      { 
         try
         {
            message = ( String ) input.readObject();
            displayMessage( "\n" + message ); 
         } 
         catch ( ClassNotFoundException classNotFoundException ) 
         {
            displayMessage( "\nTipo de objeto recebido é desconhecido" );
         } 

      } while ( !message.equals( telaServidor.textField.getText() + ">>> TERMINATE" ) );
   } 

   private void closeConnection() 
   {
      displayMessage( "\nTerminando conexão\n" );
      setTextFieldEditable( false );

      try 
      {
         output.close();
         input.close();
         connection.close();
      } 
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } 
   } 

   private void sendData( String message )
   {
      try 
      {
         output.writeObject( telaServidor.textField.getText() + ">>> " + message );
         output.flush();
         displayMessage( "\n" + telaServidor.textField.getText() + ">>> " + message );
      } 
      catch ( IOException ioException ) 
      {
         displayArea.append( "\nErro na escrita do objeto" );
      } 
   }

   private void displayMessage( final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() 
            {
               displayArea.append( messageToDisplay ); 
            }
         }
      );
   } 

   private void setTextFieldEditable( final boolean editable )
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run()
            {
               enterField.setEditable( editable );
            } 
         }  
      ); 
   } 
} 