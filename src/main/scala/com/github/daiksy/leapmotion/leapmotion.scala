package com.github.daiksy.leapmotion

import com.leapmotion.leap.Controller
import java.io.IOException

object App {

  def main(args: Array[String]) {
    // Create a sample listener and controller
    val listener: SampleListener = new SampleListener

    val controller: Controller = new Controller

    // Have the sample listener receive events from the controller
    controller.addListener(listener)

    // Keep this process running until Enter is pressed
    println("Press Enter to quit...")

    try {
      System.in.read
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }

    // Remove the sample listener when done
    controller.removeListener(listener)
  }

}