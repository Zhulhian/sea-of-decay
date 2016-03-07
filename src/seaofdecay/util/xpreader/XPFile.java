package seaofdecay.util.xpreader;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bison on 02-01-2016.
 */
public class XPFile {
    int version;
    int noLayers;
    List<XPLayer> layers;

    public XPFile(int version, int noLayers, List<XPLayer> layers) {
        this.version = version;
        this.noLayers = noLayers;
        this.layers = layers;
    }

    public void draw(AsciiPanel terminal) {
        if (layers.size() < 1)
            return;
        layers.get(0).draw(terminal);
    }

    public XPLayer layer(int i) {
        return layers.get(i);
    }

    public int noLayers() {
        return noLayers;
    }
}
