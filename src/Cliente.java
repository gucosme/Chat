import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Window.Type;

public class Cliente extends JFrame 
{
   
   private static final long serialVersionUID = 1L;
   private JTextField enterField; 
   private JTextArea displayArea; 
   private ObjectOutputStream output; 
   private ObjectInputStream input; 
   private String message = ""; 
   private String chatServer; 
   private Socket client; 
   private TelaEscolhaNick telaCliente = new TelaEscolhaNick();

   public Cliente( String host )
   {
      super( "Chat Cliente" );
      
	  telaCliente.setVisible(true);
	  telaCliente.setAlwaysOnTop(true);
	  telaCliente.setLocationRelativeTo(null);
      
      chatServer = host; 

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

      getContentPane().add( enterField, BorderLayout.NORTH );

      displayArea = new JTextArea();
      getContentPane().add( new JScrollPane( displayArea ), BorderLayout.CENTER );

      setSize( 361, 424 );
      setVisible( true );
   } 

   public void runClient() 
   {
      try
      {
         connectToServer();
         getStreams(); 
         processConnection();
      }
      catch ( EOFException eofException ) 
      {
         displayMessage( "\nConexão com Cliente terminada" );
      }
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      }
      finally 
      {
         closeConnection();
      } 
   } 

   private void connectToServer() throws IOException
   {      
      displayMessage( "Tentando conectar\n" );

      client = new Socket( InetAddress.getByName( chatServer ), 12345 );

      displayMessage( "Conectado com: " + 
         client.getInetAddress().getHostName() );
   } 

   private void getStreams() throws IOException
   {
      output = new ObjectOutputStream( client.getOutputStream() );      
      output.flush();

      input = new ObjectInputStream( client.getInputStream() );
   }

   private void processConnection() throws IOException
   {
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

      } while ( !message.equals( ">>> Terminado" ) );
   }

   private void closeConnection() 
   {
      displayMessage( "\nFechando conexão" );
      setTextFieldEditable( false );

      try 
      {
         output.close();
         input.close();
         client.close();
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
         output.writeObject( ">>> " + message );
         output.flush();
         displayMessage( "\n>>> " + message );
      } 
      catch ( IOException ioException )
      {
         displayArea.append( "\nErro ao escrever o objeto" );
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
