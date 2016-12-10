import swing._
import swing.event._
import swing.BorderPanel.Position._
import event._
import scala.io.Source
import java.util.function.Consumer
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.io.File

import scala.reflect.io.Directory

object ScalaSwingGUIClass extends SimpleSwingApplication {
  var folder_list = List[String]()

  var current_folder = "default_name"
  var keyword = ""
  var lang = ""

  var stats_label_string = ""

  def getLanguage(): String = {
    return lang
  }

  def getKeyword(): String = {
    return keyword
  }

  def getCurrentFolder(): String = {
    return current_folder
  }

  def getStatsLabelString(): String = {
    return stats_label_string
  }

  def setupFolderList(add_initial: Boolean): Unit = {
    folder_list = new File("repo_projects").listFiles.filter(_.isDirectory).map(_.getName).toList

    if (add_initial == true)
      folder_list = "<<<<SELECT FOLDER>>>>" :: folder_list
  }

  def top = new MainFrame {

    setupFolderList(true)

    title = "Main GUI"
    preferredSize = new Dimension(640, 480)
    //https://www.cis.upenn.edu/~matuszek/Concise%20Guides/Concise%20Scala%20GUI.html
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Exit") {
          sys.exit(0)
        })
      }
    }

    val dl_btn = new Button {
      preferredSize = new Dimension(60, 60)
      text = "Download"
    }

    //val delet

    val refresh_btn = new Button {
      text = "Refresh"
    }

    val showstats_btn = new Button {
      preferredSize = new Dimension(60, 60)
      text = "Statistics"
    }

    val delete_btn = new Button {
      preferredSize = new Dimension(60, 60)
      text = "Delete"
    }

    val keyword_textbox = new TextField {
      columns = 1
      text = "Keyword"
    }

    val stats_label = new TextArea {
      text = "No stats to show yet."
      columns = 3
    }

    var folder_combobox = new ListView(folder_list)

    val stats_panel = new BorderPanel {
      add(stats_label, BorderPanel.Position.Center)
    }

    val folder_gridbox = new GridPanel(1, 3) {
      contents += showstats_btn
      contents += refresh_btn
      contents += delete_btn
    }

    val main_gridbox = new GridPanel(2, 2) {
      contents += keyword_textbox
      contents += dl_btn
      contents += folder_combobox
      contents += folder_gridbox
    }

    contents = new BorderPanel {
      layout(main_gridbox) = North
      layout(stats_panel) = Center
    }

    listenTo(dl_btn)
    listenTo(refresh_btn)
    listenTo(showstats_btn)
    listenTo(delete_btn)
    listenTo(folder_combobox.mouse.clicks, folder_combobox.selection)

    reactions += {
      case ButtonClicked(component)
        if component == dl_btn => {
        keyword = keyword_textbox.text
        doDownloading()
      }
      case ButtonClicked(component)
        if component == refresh_btn => {
        setupFolderList(true)
        folder_combobox.listData = folder_list
      }
      case ButtonClicked(component)
        if component == delete_btn => {
        val path = new File("repo_projects/" + current_folder)
        delete(path)
        stats_label.text = getStatsLabelString()
      }
      case ButtonClicked(component)
        if component == showstats_btn => {
        setStatsForSelection()
        stats_label.text = getStatsLabelString()
      }
      case ListSelectionChanged(folder_combobox, range, live) => {
        if (folder_combobox.peer.getSelectedIndex() <= folder_list.size - 1 &&
          folder_combobox.peer.getSelectedIndex() >= 0)
          current_folder = folder_combobox.listData(folder_combobox.peer.getSelectedIndex()).toString()
      }
    }
  }

  def doDownloading(): Unit = {
    Console.println(keyword)
    val r = new Request()
    r.fetchKeyword(getKeyword())
  }

  def setStatsForSelection(): Unit = {
    if (current_folder == "<<<<SELECT FOLDER>>>>") {
      stats_label_string = "Please select a folder from the drop down menu..."
      return
    }
    stats_label_string = ""
    val folder_path = "repo_projects/" + current_folder + "/Analysis"
    val file_name = "output.txt"
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
    for (line <- file_opened.getLines()) {
      stats_label_string = stats_label_string + line + "\n"
    }
    file_opened.close()
  }

  def delete(file: File) {
    if (!file.exists()) {
            stats_label_string = "Cannot find folder path! Please ensure repo folder exists!"
            return
          }
    if (file.isDirectory)
      Option(file.listFiles).map(_.toList).getOrElse(Nil).foreach(delete(_))
    file.delete
  }
}



