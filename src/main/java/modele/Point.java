/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author etine
 */
public class Point {
    private int x; 
    private int y; 
    
    Point(int _x, int _y){
        this.x=_x; 
        this.y=_y; 
    }
    
    public void setPosition(int _x, int _y){
        this.x=_x; 
        this.y=_y; 
    }
    
    public int getPositionX(){
        return this.x; 
    }
    
    public int getPositionY(){
        return this.y; 
    }
}
