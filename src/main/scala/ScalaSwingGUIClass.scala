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
  // list of all folders in repo_projects directory stored
  // as strings (current state of subdirectories one level down from
  // repo_projects)
  var folder_list = List[String]()
  // the current folder that is selected in the ListView object
  var current_folder = "default_name"
  // the keyword chosen by the text box at the top right of the screen
  // for downloading repositories
  var keyword = ""
  // unused
  var lang = ""
  // string representing statistics to be displayed on GUI (about a particular
  // repository)
  var stats_label_string = ""
  
  //unused
  def getLanguage(): String = {
    return lang
  }

  // The next 3 functions return aforementioned variables
  def getKeyword(): String = {
    return keyword
  }

  def getCurrentFolder(): String = {
    return current_folder
  }

  def getStatsLabelString(): String = {
    return stats_label_string
  }

  // 1) popoulate folder_list with all folders (initially empty)
  // 2) make sure folder list always has 1 element (first one not representing a folder, but an indicator 
  //    to select a folder)
  def setupFolderList(add_initial: Boolean): Unit = {
    // filter by list
    folder_list = new File("repo_projects").listFiles.filter(_.isDirectory).map(_.getName).toList

    if (add_initial == true)
      folder_list = "<<<<SELECT FOLDER>>>>" :: folder_list
  }
  // function triggering main event loop is called "top"
  def top = new MainFrame {
    // init folder list
    setupFolderList(true)

    title = "Main GUI"
    preferredSize = new Dimension(640, 480)
    //https://www.cis.upenn.edu/~matuszek/Concise%20Guides/Concise%20Scala%20GUI.html
    //simple menu bar allowing user to exit
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Exit") {
          sys.exit(0)
        })
      }
    }
    
    // what follows are declarations of various button objects
    // containing what the button text should display
    // /start
    
    val dl_btn = new Button {
      preferredSize = new Dimension(60, 60)
      text = "Download"
    }

    // refreshes the folder list
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
    
    // end
    
    // This is the textbox where the user inputs the keyword

    val keyword_textbox = new TextField {
      columns = 1
      text = "Keyword"
    }


    // The large portion of the GUI showing the repo statistics
    
    val stats_label = new TextArea {
      text = "No stats to show yet."
      columns = 3
    }

    // The drop down list of folders to select
    var folder_combobox = new ListView(folder_list)

    // panel holding the stats label
    val stats_panel = new BorderPanel {
      add(stats_label, BorderPanel.Position.Center)
    }

    // The gridboxes are necessary to organize the layout of the GUI
    
    // This gridbox consists of buttons that manipulate a repo
    val folder_gridbox = new GridPanel(1, 3) {
      contents += showstats_btn
      contents += refresh_btn
      contents += delete_btn
    }
    
    // This gridbox consists of buttons that download a repo and the
    // gridbox above
    val main_gridbox = new GridPanel(2, 2) {
      contents += keyword_textbox
      contents += dl_btn
      contents += folder_combobox
      contents += folder_gridbox
    }
    
    // add the widgets to the GUI
    contents = new BorderPanel {
      layout(main_gridbox) = North
      layout(stats_panel) = Center
    }

    // make all widgets active
    listenTo(dl_btn)
    listenTo(refresh_btn)
    listenTo(showstats_btn)
    listenTo(delete_btn)
    listenTo(folder_combobox.mouse.clicks, folder_combobox.selection)

    // define widget actions
    reactions += {
      // download button clicked
      case ButtonClicked(component)
        if component == dl_btn => {
        keyword = keyword_textbox.text
        doDownloading()
      }
      // refresh button clicked (that refreshes the folder list)
      case ButtonClicked(component)
        if component == refresh_btn => {
        // reinitialize the folder list
        setupFolderList(true)
        // reinitialize the contents of the drop down list representing the folder list
        folder_combobox.listData = folder_list
      }
      // delete repo (of selected folder)
      case ButtonClicked(component)
        if component == delete_btn => {
        // get the path
        val path = new File("repo_projects/" + current_folder)
        // recursively delete all folders and files in path
        delete(path)
        // reset the stats text label so that it doesn't display outdated information
        stats_label.text = getStatsLabelString()
      }
      // display statistics of current folder selected from drop down list
      case ButtonClicked(component)
        if component == showstats_btn => {
        // see comments below
        setStatsForSelection()
        // update the label
        stats_label.text = getStatsLabelString()
      }
      // when a folder is selected from drop down list , update current folder
      // , but first check to see if index is in range (since we may have added or deleted a folder)
      case ListSelectionChanged(folder_combobox, range, live) => {
        if (folder_combobox.peer.getSelectedIndex() <= folder_list.size - 1 &&
          folder_combobox.peer.getSelectedIndex() >= 0)
          // set by index
          current_folder = folder_combobox.listData(folder_combobox.peer.getSelectedIndex()).toString()
      }
    }
  }

  // call backend of system that downloads the repo, creates UDB files, etc... via
  // Akka API calls
  def doDownloading(): Unit = {
    Console.println(keyword)
    val r = new Request()
    r.fetchKeyword(getKeyword())
  }

  // checks to see that the stats folder and file exists, then set contents to string based on file contents to
  // be printed to screen later
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
    // stats string is populated with characters separated by newlines
    // that correspond to the newlines in the stats file
    //
    // Note:
    //
    // We need to use a TextArea object and not a Label object (as our label) to display the newlines accurately
    // since the Label text field does not store newline characters.
    val file_opened = Source.fromFile(total_path)
    // Append lines in a loop (to the string that will be printed on the TextArea )
    for (line <- file_opened.getLines()) {
      stats_label_string = stats_label_string + line + "\n"
    }
    file_opened.close()
  }

  // recursively delete all files in downloaded repository, subfolders, and then the entire folder.
  def delete(file: File) {
    if (!file.exists()) {
            stats_label_string = "Cannot find folder path! Please ensure repo folder exists!"
            return
          }
    // inspired by stackoverflow
    if (file.isDirectory)
      Option(file.listFiles).map(_.toList).getOrElse(Nil).foreach(delete(_))
    file.delete
  }
}