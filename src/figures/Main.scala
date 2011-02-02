package figures

import scala.swing.SimpleSwingApplication

import ui._

object Main extends SimpleSwingApplication {
  def top = new FigureFrame with RectangleSupport
}

// vim: set ts=4 sw=4 et:
