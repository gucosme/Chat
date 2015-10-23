import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class TelaEscolhaNick extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnLogar;
	private JButton btnCancelar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaEscolhaNick frame = new TelaEscolhaNick();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TelaEscolhaNick() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 306);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Icon img = new ImageIcon("fundoEscolhaNick.fw.png");
		
		textField = new JTextField();
		textField.setBounds(117, 169, 226, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnLogar = new JButton("Logar");
		btnLogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Informe algum nick!", "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLogar.setBounds(142, 247, 89, 23);
		contentPane.add(btnLogar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnCancelar.setBounds(233, 247, 89, 23);
		contentPane.add(btnCancelar);
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(img);
		lblNewLabel.setBounds(0, 0, 453, 306);
		contentPane.add(lblNewLabel);
	}
}
