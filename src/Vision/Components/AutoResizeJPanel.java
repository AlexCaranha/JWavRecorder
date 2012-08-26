package Vision.Components;

import java.awt.*;
import javax.swing.*;

public class AutoResizeJPanel extends JPanel {
    private Image imagem;

    public AutoResizeJPanel(String strCaminhoImagem) {        
        imagem = Toolkit.getDefaultToolkit().getImage(Menu.class.getResource(strCaminhoImagem));
    }

    public void paint(Graphics g) {
        super.paint(g);
        // the size of the component
        Dimension d = getSize();
        // the internal margins of the component
        Insets i = getInsets();
        // draw to fill the entire component
        g.drawImage(imagem, i.left, i.top, d.width - i.left - i.right, d.height - i.top - i.bottom, this );
    }
}