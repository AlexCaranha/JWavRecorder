package Vision.Components;

import Model.IObserver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Alex Libório Caranha
 */
public class XYGraph extends JPanel implements IObserver {    
    private XYPlot          plot;
    private JFreeChart      chart;
    private ChartPanel      chartPanel;
        
    public XYGraph(String title, 
                    String titleX, 
                    String titleY, 
                    XYSeries signal) {
        
        super(new BorderLayout());
        
        chart = createChart(title, titleX, titleY, signal);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        /*
        this.slider = new JSlider(0, 100, 0);
        this.slider.addChangeListener(this);
        */
        this.add(chartPanel, BorderLayout.CENTER);
        //this.add(slider, BorderLayout.SOUTH);
    }
    
    private JFreeChart createChart(String title, 
                                    String titleX, 
                                    String titleY, 
                                    XYSeries signal) {
        
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        dataset.addSeries(signal.getKey(), signal.toArray());
                
        JFreeChart c = ChartFactory.createXYLineChart(
            title, titleX, titleY,
            dataset,
            PlotOrientation.VERTICAL,  
            true,
            true,
            false
        );

        c.setBackgroundPaint(Color.white);
        
        plot = c.getXYPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setLegendLine(new Rectangle2D.Double(-4.0, -3.0, 10.0, 6.0));
        renderer.setSeriesPaint(0, Color.black);
        
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        
        return c;
    }
    
    @Override
    public void update(Object ... parameters) {
        if (parameters == null) return;
        if (parameters.length > 2) return;
        
        if (((String) parameters[0]).equalsIgnoreCase("XYSeries")) {
            XYSeries serie = (XYSeries) parameters[1];

            chart = createChart(
                                    "Waveform", 
                                    "time (in seconds)", 
                                    "amplitude", 
                                    serie
                                );
            chartPanel.setChart(chart);
        }
        else
        if (((String) parameters[0]).equalsIgnoreCase("Line")) {
            if (parameters[1].getClass().getCanonicalName().contains("Double")){
                plot.setDomainCrosshairValue((Double) parameters[1]);
            }else{
                plot.setDomainCrosshairVisible((boolean) parameters[1]);
            }
        }
        this.repaint();
    }
}