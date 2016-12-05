import swing._
import swing.BorderPanel.Position._
import event._

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path


object ScalaSwingGUIClass extends SimpleSwingApplication {
  var folder_list = List[String]()

  var keyword = ""
  var lang = ""

  def getLanguage() : String = {
    return lang
  }

  def getKeyword() : String = {
    return keyword
  }

//  def initFolderList() : Unit = {
//    val list_of_version_subfolders = new File(test_java_file_path).listFiles.filter(_.isDirectory).map(_.getName)
//  }

  def top = new MainFrame {

//    initFolderList()

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

    val stats_label = new Label {
      text = "Stats"
    }

    val folder_combobox = new ComboBox(List("1","2","3"))

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
    }

    listenTo(dl_btn)

    reactions += {
      case ButtonClicked(component)
        if component == dl_btn => {
        keyword = keyword_textbox.text
        doDownloading()
      }
    }
  }

  def doDownloading(): Unit = {
    Console.println(getKeyword())

    val r= new Request()
    r.fetchKeyword(getKeyword())
  }

}

