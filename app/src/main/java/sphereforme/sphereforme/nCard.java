package sphereforme.sphereforme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by smfields on 1/21/17.
 */

public class nCard {

    private HashMap<String, String> attr;
    private String header;

    public nCard() {
        attr = new HashMap<String, String>();
    }

    @Override
    public String toString() {
        String format = "#CARD#\n";

        Set<Map.Entry<String, String>> set = attr.entrySet();
        for(Map.Entry<String, String> e : set) {
            format += e.getKey() + ": " + e.getValue() + "\n";
        }

        format += "#CARD#";

        return format;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void addAttr(String key, String value) {
        attr.put(key, value);
    }

    public String getAttr(String key) {
        return attr.get(key);
    }

}
