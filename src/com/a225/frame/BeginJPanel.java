package com.a225.frame;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.a225.main.GameController;
import com.a225.main.GameStart;
import com.a225.model.loader.ElementLoader;

public class BeginJPanel extends JPanel{
	private ImageIcon img;
	private int w;
	private int h;
	
	//无参构造函数，防止出错
	public BeginJPanel(){}
	
	public BeginJPanel(String url){
		List<String> str = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = new ImageIcon(url);
		this.w = new Integer(str.get(0)).intValue();
		this.h = new Integer(str.get(1)).intValue();
		init();
	}

	private void init() {

		this.setLayout(null);
		
		JLabel jLabel = new JLabel(img);
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		jLabel.setBounds(0, 0, w, h);
		
		JButton onePlayerButton = new JButton();
		onePlayerButton.setIcon(new ImageIcon("img/bg/rect1.png"));
		onePlayerButton.setBounds(w/6, h-h/2, 180, 60);
		onePlayerButton.setBorderPainted(false);
		onePlayerButton.setFocusPainted(false);
		onePlayerButton.setContentAreaFilled(false);
		onePlayerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameStart.changeJPanel(true);
			}
		});
		
		JButton twoPlayerButton = new JButton();
		twoPlayerButton.setIcon(new ImageIcon("img/bg/rect2.png"));
		twoPlayerButton.setBounds(w/6, h-h/4, 180, 60);
		twoPlayerButton.setBorderPainted(false);
		twoPlayerButton.setFocusPainted(false);
		twoPlayerButton.setContentAreaFilled(false);
		twoPlayerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameStart.changeJPanel(true);
			}
		});
		
		this.add(onePlayerButton);
		this.add(twoPlayerButton);
		this.add(jLabel);
		
		this.setVisible(true);
		this.setOpaque(true);
	}
	
}
