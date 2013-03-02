package com.bitwaffle.guts.desktop.scala.builder

import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.Label
import com.bitwaffle.guts.Game
import java.awt.Panel
import com.bitwaffle.guts.input.Keys
import com.bitwaffle.guts.gui.GUI

object Builder {
	private val koonami: Array[Keys] = Array(
	    Keys.DPAD_UP, Keys.DPAD_UP, Keys.DPAD_DOWN, Keys.DPAD_DOWN,
	    Keys.DPAD_LEFT, Keys.DPAD_RIGHT, Keys.DPAD_LEFT, Keys.DPAD_RIGHT,
	    Keys.B, Keys.A, Keys.ENTER)
	private var currentKey = 0
	private var currentKeyDown = false
	private var frameCreated = false
	
	lazy val frame = new Frame("Guts Builder")
	lazy val numEntsLabel, fpsLabel = new Label
  
	def create: Unit = {
	  if(!frameCreated){
		  frame setSize (500, 500)
		  frame addWindowListener(new WindowAdapter{
		    override def windowClosing(we: WindowEvent){
		      frame setVisible false
		    }
		  })
		  frame add createInfoPanel
		  frameCreated = true
	  }
	  
	  frame setVisible true
	}
	
	private def createInfoPanel: Panel = {
	  val infoPanel = new Panel
	  
	  infoPanel add numEntsLabel
	  infoPanel add fpsLabel
	  
	  infoPanel
	}
	
	def update {
	  if(!(frame isVisible) && Game.gui.isCurrentState(GUI.States.TITLESCREEN)){
		   if(koonami(currentKey) isPressed){
		    currentKeyDown = true
		  } else if(currentKeyDown && !(koonami(currentKey) isPressed)){
			 currentKeyDown = false
			 currentKey += 1
			 if(currentKey == koonami.length){
				 currentKey = 0
				 create
			 }
		  } 
	  }
	  
	  numEntsLabel setText "Num Ents:    "+Game.physics.numEntities()
	  fpsLabel     setText "Current FPS: "+Game.currentFPS
	}
}





