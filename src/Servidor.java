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
   private String s = new TelaEscolhaNick().n.getNickName(); 

   public Servidor()
   {
      setTitle(s);

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
   
   public static String atribuirNick(Nick nick){
	   return nick.getNickName();
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
               displayMessage( "\nServer terminated connection" );
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
      displayMessage( "Waiting for connection\n" );
      connection = server.accept();           
      displayMessage( "Connection " + counter + " received from: " +
         connection.getInetAddress().getHostName() );
   }

   private void getStreams() throws IOException
   {
      output = new ObjectOutputStream( connection.getOutputStream() );
      output.flush();

      input = new ObjectInputStream( connection.getInputStream() );

      displayMessage( "\nGot I/O streams\n" );
   }

   private void processConnection() throws IOException
   {
      String message = "Connection successful";
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
            displayMessage( "\nUnknown object type received" );
         } 

      } while ( !message.equals( "CLIENT>>> TERMINATE" ) );
   } 

   private void closeConnection() 
   {
      displayMessage( "\nTerminating connection\n" );
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
         output.writeObject( "SERVER>>> " + message );
         output.flush();
         displayMessage( "\nSERVER>>> " + message );
      } 
      catch ( IOException ioException ) 
      {
         displayArea.append( "\nError writing object" );
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