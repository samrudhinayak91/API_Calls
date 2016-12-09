import swing._
import swing.event._
import swing.BorderPanel.Position._
import event._

import scala.io.Source

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path

import java.io.File

object ScalaSwingGUIClass extends SimpleSwingApplication {
  var folder_list = List[String]()

  var current_folder = "default_name"
  var keyword = ""
  var lang = ""

  var stats_label_string = ""

  def getLanguage() : String = {
    return lang
  }

  def getKeyword() : String = {
    return keyword
  }

  def getCurrentFolder() : String = {
    return current_folder
  }

  def getStatsLabelString() : String = {
    return stats_label_string
  }

  def setupFolderList() : Unit = {
    folder_list = new File("repo_projects").listFiles.filter(_.isDirectory).map(_.getName).toList
  }

  def top = new MainFrame {

    setupFolderList()

    title = "Main GUI"
    preferredSize = new Dimension(640,480)
    //https://www.cis.upenn.edu/~matuszek/Concise%20Guides/Concise%20Scala%20GUI.html
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Exit") {
          sys.exit(0)
        })
      }
    }

    val dl_btn = new Button {
      preferredSize = new Dimension(60,60)
      text = "Download"
    }

    //val delet

    val refresh_btn = new Button {
      text = "Refresh"
    }

    val showstats_btn = new Button {
      preferredSize = new Dimension(60,60)
      text = "Statistics"
    }

    val keyword_textbox = new TextField {
      columns = 1
      text = "Keyword"
    }

    val stats_scrollbar = new ScrollBar {
      orientation = Orientation.Vertical
    }

    val stats_label = new TextArea {
      text = "No stats to show yet."
    }

    val folder_combobox = new ComboBox(folder_list)

    current_folder = folder_combobox.peer.getSelectedItem().toString()

    val stats_panel = new BorderPanel {
      add(stats_label,BorderPanel.Position.Center)
      add(stats_scrollbar,BorderPanel.Position.East)
    }

    val main_gridbox = new GridPanel(2,2) {
      contents += keyword_textbox
      contents += dl_btn
      contents += folder_combobox
      contents += showstats_btn
    }

    contents = new BorderPanel {
      layout(main_gridbox) = North
      layout(stats_panel) = Center
      layout(refresh_btn) = East
    }

    listenTo(dl_btn)
    listenTo(refresh_btn)
    listenTo(showstats_btn)
    listenTo(folder_combobox.mouse.clicks,folder_combobox.selection)

    reactions += {
      case ButtonClicked(component)
        if component == dl_btn =>
        {
          keyword = keyword_textbox.text
          doDownloading()
        }
      case ButtonClicked(component)
        if component == refresh_btn =>
        {
          setupFolderList()
          //folder_combobox.peer.setEditable(true)
          //folder_combobox = new ComboBox(folder_list)
        }
      case ButtonClicked(component)
        if component == showstats_btn =>
        {
          setStatsForSelection()
          stats_label.text = getStatsLabelString()
        }
      case SelectionChanged(`folder_combobox`) =>
        {
          current_folder = folder_combobox.peer.getSelectedItem().toString()
        }
    }
  }

  def doDownloading(): Unit = {
    Console.println(keyword)
    // send the keyword to form the HTTP request
    val r= new Request()
    r.fetchKeyword(getKeyword())

  }

  def setStatsForSelection(): Unit = {
    stats_label_string = ""
    val folder_path = "repo_projects/" + current_folder + "/Analysis"
    val file_name = "stats.txt"
    val total_path = folder_path + "/" + file_name
    val folder_path_exists = Files.exists(Paths.get(folder_path))
    val file_exists = new File(total_path).exists
    if (!folder_path_exists) {
      stats_label_string = "Cannot find folder path! Please ensure Analysis folder exists."
      return
    }
    if (!file_exists) {
      stats_label_string = "Found folder, but cannot find file!. Make sure stats file exists."
      return
    }
    val file_opened = Source.fromFile(total_path)
    for(line <- file_opened.getLines()) {
      stats_label_string = stats_label_string + line + "\n"
    }
    file_opened.close()
  }

}

