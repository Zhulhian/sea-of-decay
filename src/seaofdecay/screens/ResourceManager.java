package seaofdecay.screens;


import seaofdecay.util.asciipanel.AsciiPanel;
import seaofdecay.util.xpreader.REXReader;
import seaofdecay.util.xpreader.XPFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * A Resource Manager that gets resources from the res folder in the project root.
 * You can access the resource by calling getRes(String filename).
 * */
public class ResourceManager {
    static final File RES_DIR = new File("res");
    private Map<String, XPFile> resMap = new HashMap<>();

    public ResourceManager() {
	if (RES_DIR.isDirectory() && RES_DIR.exists() && (RES_DIR.list().length > 0)) {
	    for (File file : RES_DIR.listFiles()) {
		try {
		    if (file.getName().toLowerCase().endsWith(".xp")) {
			resMap.put(file.getName(), REXReader.loadXP('/' + file.getName()));
		    }
		} catch (IOException io) {
		    System.out.println("IOEXCEPTION: " + io);
		} catch (DataFormatException dfe) {
		    System.out.println("DataFormatException: " + dfe);
		}
	    }
	} else {
	    System.out.println("No files in resource directory, or directory not found!");
	}
    }

    public void drawRes(AsciiPanel terminal, String res) {
	drawRes(terminal, res, 0);
    }

    public void drawRes(AsciiPanel terminal, String res, int layer) {
	resMap.get(res).layer(layer).draw(terminal);
    }

    public XPFile getRes(String res) {
	return resMap.get(res);
    }

}
