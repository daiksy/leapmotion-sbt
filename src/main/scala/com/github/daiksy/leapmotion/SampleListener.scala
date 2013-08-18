package com.github.daiksy.leapmotion

import com.leapmotion.leap.Gesture.State
import com.leapmotion.leap.Gesture.Type._
import com.leapmotion.leap._
import scala.collection.JavaConverters._

class SampleListener extends Listener {
  override def onInit(controller: Controller): Unit = {
    println("Initialized")
  }

  override def onConnect(controller: Controller): Unit = {
    println("Connected")
    controller.enableGesture(Gesture.Type.TYPE_SWIPE)
    controller.enableGesture(Gesture.Type.TYPE_CIRCLE)
    controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP)
    controller.enableGesture(Gesture.Type.TYPE_KEY_TAP)
  }

  override def onDisconnect(controller: Controller): Unit = {
    println("Disconnected")
  }

  override def onExit(controller: Controller): Unit = {
    println("Exited")
  }

  override def onFrame(controller: Controller): Unit = {
    val frame = controller.frame
    println("Frame id: " + frame.id
      + ", timestamp: " + frame.timestamp
      + ", hands: " + frame.hands.count
      + ", fingers: " + frame.fingers.count
      + ", tools: " + frame.tools.count
      + ", gestures " + frame.gestures.count)

    if (!frame.hands.empty) {
      val hand = frame.hands.get(0)
      val fingers = hand.fingers
      if (!fingers.empty) {
        var avgPos = Vector.zero
        import scala.collection.JavaConversions._
        for (finger <- fingers) {
          avgPos = avgPos.plus(finger.tipPosition)
        }
        avgPos = avgPos.divide(fingers.count)
        println("Hand has " + fingers.count + " fingers, average finger tip position: " + avgPos)
      }
      println("Hand sphere radius: " + hand.sphereRadius + " mm, palm position: " + hand.palmPosition)
      val normal = hand.palmNormal
      val direction = hand.direction
      println("Hand pitch: " + Math.toDegrees(direction.pitch) + " degrees, " +
        "roll: " + Math.toDegrees(normal.roll) + " degrees, " +
        "yaw: " + Math.toDegrees(direction.yaw) + " degrees")
    }

    val gestures = frame.gestures
    for (gesture <- frame.gestures().asScala) {
      gesture.`type` match {
        case TYPE_CIRCLE =>
          val circle = new CircleGesture(gesture)

          val clockwiseness = if (circle.pointable.direction.angleTo(circle.normal) <= Math.PI / 4) {
            "clockwise"
          } else {
            "counterclockwise"
          }

          val sweptAngle: Double = if (circle.state ne State.STATE_START) {
            val previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id))
            (circle.progress - previousUpdate.progress) * 2 * Math.PI
          } else 0

          println("Circle id: " + circle.id + ", " + circle.state +
            ", progress: " + circle.progress +
            ", radius: " + circle.radius +
            ", angle: " + Math.toDegrees(sweptAngle) + ", " + clockwiseness)
        case TYPE_SWIPE =>
          val swipe = new SwipeGesture(gesture);
          println("Swipe id: " + swipe.id + ", " + swipe.state +
            ", position: " + swipe.position +
            ", direction: " + swipe.direction +
            ", speed: " + swipe.speed)
        case TYPE_SCREEN_TAP =>
          val screenTap = new ScreenTapGesture(gesture)
          println("Screen Tap id: " + screenTap.id + ", " + screenTap.state +
            ", position: " + screenTap.position +
            ", direction: " + screenTap.direction)
        case TYPE_KEY_TAP =>
          val keyTap = new KeyTapGesture(gesture)
          println("Key Tap id: " + keyTap.id + ", " + keyTap.state +
            ", position: " + keyTap.position +
            ", direction: " + keyTap.direction)
        case _ =>
          println("Unknown gesture type.")
      }
    }
    if (!frame.hands.empty || !gestures.empty) {
      println
    }
  }
}