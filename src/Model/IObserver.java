/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author alexcaranha
 */
public interface IObserver {
    public void update(Object ... parameters);
    public String getName();
}
