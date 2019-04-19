package com.a225.frame;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.a225.main.GameController;
import com.a225.main.GameStart;
import com.a225.model.loader.ElementLoader;

public class BeginJPanel extends JPanel{
	private ImageIcon img;
	private int w;
	private int h;
	
	//¹¹Ôìº¯Êý
	public BeginJPanel(){
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = ElementLoader.getElementLoader().getImageMap().get("beginBackground");
		this.w = new Integer(data.get(0)).intValue();
		this.h = new Integer(data.get(1)).intValue();
		init();
	}

	private void init() {

		this.setLayout(null);
		
		JLabel jLabel = new JLabel(img);
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		jLabel.setBounds(0, 0, w, h);
		
		JButton onePlayerButton = new JButton();
		onePlayerButton.setIcon(ElementLoader.getElementLoader().getImageMap().get("rect1"));
		onePlayerButton.setBounds(w/6, h-h/2, 180, 60);
		onePlayerButton.setBorderPainted(false);
		onePlayerButton.setFocusPainted(false);
		onePlayerButton.setContentAreaFilled(false);
		onePlayerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameController.setTwoPlayer(false);
				GameStart.startNewGame();
			}
		});
		
		JButton twoPlayerButton = new JButton();
		twoPlayerButton.setIcon(ElementLoader.getElementLoader().getImageMap().get("rect2"));
		twoPlayerButton.setBounds(w/6, h-h/4, 180, 60);
		twoPlayerButton.setBorderPainted(false);
		twoPlayerButton.setFocusPainted(false);
		twoPlayerButton.setContentAreaFilled(false);
		twoPlayerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameController.setTwoPlayer(true);
				GameStart.startNewGame();
			}
		});
		
		this.add(onePlayerButton);
		this.add(twoPlayerButton);
		this.add(jLabel);
		
		this.setVisible(true);
		this.setOpaque(true);
	}
	
}
