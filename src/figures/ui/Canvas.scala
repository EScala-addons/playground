package figures.ui

import java.awt.{Component,Graphics2D}

import scala.swing.Panel

import figures.model.MutableDrawing

trait Canvas {
  def paint(g: Graphics2D): Unit
  def peer: Component
  val drawing: MutableDrawing
}

// vim: set ts=4 sw=4 et:
