package com.bitwaffle.guts.desktop.devmode.builder

import java.awt.BorderLayout
import java.awt.Label
import java.awt.Panel
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import com.badlogic.gdx.math.Vector2
import com.bitwaffle.guts.Game
import com.bitwaffle.guts.Game
import com.bitwaffle.guts.desktop.devmode.DevModeDisplay
import com.bitwaffle.guts.graphics.Render2D
import com.bitwaffle.guts.graphics.camera.Camera
import com.bitwaffle.guts.gui.GUI
import com.bitwaffle.offworld.entities.dynamic.BreakableRock

import javax.swing.BoxLayout
import javax.swing.JButton

object Builder {
  /*
	private val koonami: Array[Int] = Array(
		Input.Keys.UP, Keys.DPAD_UP, Keys.DPAD_DOWN, Keys.DPAD_DOWN,
		Keys.DPAD_LEFT, Keys.DPAD_RIGHT, Keys.DPAD_LEFT, Keys.DPAD_RIGHT,
		Keys.B, Keys.A, Keys.ENTER)
		*/
	private var currentKey = 0
	private var currentKeyDown = false

	/** Labels that hold info about the game */
	lazy val numEntsLabel, fpsLabel = new Label

	/**
	 * Updates the builder.
	 * Checks for the koonami code and starts dev mode if it's entered
	 */
	def update {
		if (Game.gui.isCurrentState(GUI.States.TITLESCREEN)) {
		  /*
			if (koonami(currentKey) isPressed) {
				currentKeyDown = true
			} else if (currentKeyDown && !(koonami(currentKey) isPressed)) {
				currentKeyDown = false
				currentKey += 1
				if (currentKey == koonami.length) {
					currentKey = 0
					enterDevMode
				}
			}
			*/
		}

		numEntsLabel setText "Num Ents:    " + Game.physics.numEntities()
		fpsLabel setText "Current FPS: " + Game.currentFPS
	}

	/**
	 * Adds things to the DevModeDisplay frame and validates it
	 */
	def enterDevMode {
		DevModeDisplay.frame add (createInfoPanel, BorderLayout.WEST)
		DevModeDisplay.frame.validate()
	}

	/**
	 * Info panel contains FPS and number of entities
	 */
	private def createInfoPanel: Panel = {
		val infoPanel = new Panel

		infoPanel setLayout new BoxLayout(infoPanel, BoxLayout.Y_AXIS)

		infoPanel add numEntsLabel
		infoPanel add fpsLabel
		infoPanel add createToolPanel

		infoPanel
	}
	
	private def createToolPanel = {
		val toolPanel, leftTools, rightTools = new Panel
		
		leftTools setLayout new BoxLayout(leftTools, BoxLayout.Y_AXIS);
		val cameraButt = new JButton("Camera")
		cameraButt.addActionListener(new ActionListener(){
			override def actionPerformed(e: ActionEvent){
				Render2D.camera.setMode(Camera.Modes.FREE)
			}
		})
		leftTools add cameraButt
		
		rightTools setLayout new BoxLayout(rightTools, BoxLayout.Y_AXIS);
		val rockButt = new JButton("Rock")
		rockButt.addActionListener(new ActionListener{
			override def actionPerformed(e: ActionEvent){
				val rockX = Game.random.nextFloat * 10.0f - 50.0f
				val rockY = Game.random.nextFloat * 150.0f + 15.0f
				val layer = 4
				val rock = new BreakableRock(new Vector2(rockX, rockY), 1.0f, layer);
				Game.physics.addEntity(rock, true);
			}
		})
		rightTools add rockButt
		
		toolPanel add leftTools
		toolPanel add rightTools
		toolPanel
	}
}





