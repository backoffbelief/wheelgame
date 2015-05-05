package com.kael.gun.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.google.protobuf.Message;
import com.kael.req.ReqProto.LoginProto;
import com.kael.req.ReqProto.ReqActInGame;
import com.kael.req.ReqProto.ReqPairingProto;

public class GuiClient extends JFrame{
	private static final long serialVersionUID = 5212317756136397797L;

	private JButton jb_connect,jb_pair,jb_rechoose,jb_fire;
	private JTextField jTextField;
	private JLabel jl_name;
	private JTextArea jTextArea;
	
	private final InterClient client;
	
//	private Thread t;
	
	private final Player player;
	
	public GuiClient(){
		client = new InterClient("localhost", 10001,this);
		
		player = new Player();
		
		jb_connect = new JButton("connect");
		jb_connect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				client.startConnect();
				client.writeMsg(IMessage.create().withCode(Constants.CLIENT_LOGIN).
						withBody(LoginProto.newBuilder().setName(jTextField.getText())).
						build());
			/**	if( t == null){
					t = new Thread(new Runnable() {
						
						public void run() {
							while(true){
								try {
									client.writeMsg(IMessage.create().withCode(Constants.CLIENT_HEARTBEAT).withBody(HeartBeatProto.newBuilder()).build());
							        TimeUnit.SECONDS.sleep(45);
								} catch (Exception e2) {
									e2.printStackTrace();
									break;
								}
							}
						}
					});
				}
				*/
//				
//				
			}
		});
		jb_pair = new JButton("pair");
		jb_pair.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				client.writeMsg(IMessage.create().withCode(Constants.CLIENT_ASK_PAIRING).
						withBody(ReqPairingProto.newBuilder()).
						build());
			}
		});
		
		jb_rechoose = new JButton("choose");
		jb_rechoose.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(player.getCounter() < 100){
					return ;
				}
				client.writeMsg(IMessage.create().withCode(Constants.CLIENT_ACT_IN_ROOM).
						withBody(ReqActInGame.newBuilder().setAction(0)).
						build());
				
			}
		});
		
		jb_fire = new JButton("fire");
		jb_fire.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				client.writeMsg(IMessage.create().withCode(Constants.CLIENT_ACT_IN_ROOM).
						withBody(ReqActInGame.newBuilder().setAction(1)).
						build());
				
			}
		});
		
		jl_name = new JLabel(player.toString());
		
		jTextField = new JTextField(25);
		jTextArea = new JTextArea(30,40);
		
		this.setBounds(0, 0, 300, 500);
		this.setTitle("gun");
		
		this.getContentPane().setLayout(new BorderLayout());
		JPanel jPanel = new JPanel();
		
		jPanel.setLayout(new FlowLayout());
		jPanel.add(jTextField);
		jPanel.add(jb_connect);
		jPanel.add(jb_pair);
        
		JPanel cjPanel = new JPanel();
		
		cjPanel.setLayout(new FlowLayout());
		cjPanel.add(jl_name);
		cjPanel.add(jb_rechoose);
		cjPanel.add(jb_fire);
		
		JPanel pJPanel = new JPanel();
        pJPanel.setLayout(new BorderLayout());
        pJPanel.add(jPanel, BorderLayout.NORTH);
        pJPanel.add(cjPanel,BorderLayout.SOUTH);
		
		this.add(pJPanel,BorderLayout.NORTH);
		this.add(jTextArea,BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				new GuiClient();
			}
		});
	}

	public void print(Message msg) {
		jTextArea.setText(msg.toString());
	}

	public Player getPlayer() {
		return player;
	}
	
   public void refreshJlabel(){
	   jl_name.setText(player.toString());
   }
}
