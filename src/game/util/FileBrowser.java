package game.util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;


public class FileBrowser extends JFrame {

	
	public String GetImage(){
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		//chooser.setFileHidingEnabled(false);
		chooser.setFileFilter(new ImageFileFilter());
		//FileFilter
		
		int retVal = chooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File myFile = chooser.getSelectedFile();
			return myFile.getAbsolutePath();
		}
		return null;
				

		
	}
	public void DestroyMe(){
		this.dispose();
	}
	class ImageFileFilter extends FileFilter {

		  public boolean accept(File pathname) {
			  if (pathname.isDirectory())
			      return true;
		    if (pathname.getName().endsWith(".png"))
		      return true;
		 
		    return false;
		  }

		@Override
		public String getDescription() {
			return ".png";
			
		}
	}
}
