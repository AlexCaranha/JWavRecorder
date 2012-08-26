/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vision.Components;

import Model.IObservable;
import Model.IObserver;
import Model.ISignal;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author alexcaranha
 */

public class ProgressBar extends JPanel implements IObserver {
    private JLabel  lblBarraSuperior;
    private JPanel  pnlAmpulheta;
    
    public static Font FONTE_TITULO = new Font("Microsoft Sans Serif", Font.BOLD, 20);
    public static Font FONTE_SUBTITULO = new Font("Microsoft Sans Serif", Font.BOLD, 18);
    
    private JProgressBar jpbBarraProgresso;
    
    public ProgressBar(int x, int y, int largura, int altura){
        //----------------------------------------------------------------------
        this.setName("thisBarraProgresso");
        this.setSize(largura/3, altura/4);
        this.setLocation((largura - this.getWidth())/2, (altura - this.getHeight())/2);
        this.setLayout(null);
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        //----------------------------------------------------------------------
        lblBarraSuperior = new JLabel("Progresso...");
        lblBarraSuperior.setName("lblBarraSuperior");
        lblBarraSuperior.setFont(FONTE_SUBTITULO);
        lblBarraSuperior.setHorizontalTextPosition(SwingConstants.CENTER);
        lblBarraSuperior.setVerticalTextPosition(SwingConstants.CENTER);        
        lblBarraSuperior.setHorizontalAlignment(SwingConstants.CENTER); // centraliza horizontalmente
        lblBarraSuperior.setVerticalAlignment(SwingConstants.CENTER);   // centraliza verticalmente
        lblBarraSuperior.setSize(this.getWidth(), this.getHeight()/5);
        lblBarraSuperior.setLocation(0, 0);
        lblBarraSuperior.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        lblBarraSuperior.setOpaque(true);
        //lblBarraSuperior.setBackground(VFuncoesGraficas.corFaixa(false));
        this.add(lblBarraSuperior);
        //----------------------------------------------------------------------
        jpbBarraProgresso = new JProgressBar(0, 100);
        jpbBarraProgresso.setName("jpbBarraProgresso");
        jpbBarraProgresso.setSize(this.getWidth() - 20, this.getHeight()/6);
        jpbBarraProgresso.setLocation(10, this.getHeight() - jpbBarraProgresso.getHeight() - 10);
        jpbBarraProgresso.setValue(0);
        jpbBarraProgresso.setStringPainted(true);
        this.add(jpbBarraProgresso);
        //----------------------------------------------------------------------
        pnlAmpulheta = new AutoResizeJPanel("/Figures/ampulheta.png");
        pnlAmpulheta.setName("pnlAmpulheta");
        pnlAmpulheta.setSize((jpbBarraProgresso.getY() - 20 - (lblBarraSuperior.getY() + lblBarraSuperior.getHeight()))*2/3, 
                             jpbBarraProgresso.getY() - 20 - (lblBarraSuperior.getY() + lblBarraSuperior.getHeight())
                            );
        pnlAmpulheta.setLocation((this.getWidth() - pnlAmpulheta.getWidth())/2, lblBarraSuperior.getY() + lblBarraSuperior.getHeight() + 10);
        this.add(pnlAmpulheta);
        //----------------------------------------------------------------------
    }
    
    public void setPorcentagem(int valorPorcentagem){
        jpbBarraProgresso.setValue(valorPorcentagem);
    }
    
    public int getPorcentagem(int valorPorcentagem){
        return jpbBarraProgresso.getValue();
    }

    @Override
    public void update(Object ... parameters) {
        double porcent;
        
        if (parameters == null) return;
        if (parameters.length > 1) return;
        
        porcent = (Double) parameters[0];
        jpbBarraProgresso.setValue((int) porcent);        
        this.setVisible(porcent != 100);
        
        this.repaint();
    }
}
